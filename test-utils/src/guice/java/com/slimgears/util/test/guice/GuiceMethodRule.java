package com.slimgears.util.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Binder;
import com.google.inject.BindingAnnotation;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.binder.AnnotatedBindingBuilder;
import com.google.inject.binder.LinkedBindingBuilder;
import com.google.inject.util.Modules;
import com.slimgears.util.guice.GuiceServiceResolver;
import com.slimgears.util.test.MethodRules;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.Spy;

import javax.inject.Provider;
import javax.inject.Qualifier;
import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.function.Consumer;
import java.util.stream.Stream;

class GuiceMethodRule implements MethodRule {
    private final Module module;

    GuiceMethodRule(Module... modules) {
        this.module = Arrays.stream(modules)
                .reduce(((module1, module2) -> Modules.override(module1).with(module2)))
                .orElse(Modules.EMPTY_MODULE);
    }

    @Override
    public Statement apply(Statement base, FrameworkMethod method, Object target) {
        return new Statement() {
            @Override
            public void evaluate() throws Throwable {
                Module module = Modules.override(GuiceMethodRule.this.module).with(ModuleProvider.forMethod(method, target));
                MockitoAnnotations.initMocks(target);
                module = Modules.override(module).with(createMockModule(target));
                Injector injector = Guice.createInjector(module);
                MethodRule annotationRule = MethodRules.annotationRule(GuiceServiceResolver.forInjector(injector));
                injector.injectMembers(target);

                annotationRule.apply(base, method, target).evaluate();
            }
        };
    }

    @SafeVarargs
    public static Module createModuleFromFields(Object target, Class<? extends Annotation>... annotationClasses) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                getAllFields(target.getClass())
                        .filter(f -> Stream.of(annotationClasses).anyMatch(f::isAnnotationPresent))
                        .forEach(f -> toBinding(f, target).accept(binder()));
            }
        };
    }

    private static Module createMockModule(Object target) {
        return createModuleFromFields(target, Mock.class, Spy.class);
    }

    private static Consumer<Binder> toBinding(Field field, Object target) {
        field.setAccessible(true);
        Type fieldType = field.getGenericType();
        try {
            Object fieldValue = field.get(target);
            if (fieldType instanceof ParameterizedType) {
                ParameterizedType parameterizedType = (ParameterizedType)fieldType;
                Type rawType = parameterizedType.getRawType();
                if (rawType == Provider.class || rawType == com.google.inject.Provider.class) {
                    //noinspection unchecked
                    return binder -> toAnnotatedBinding(binder.bind(TypeLiteral.get(parameterizedType.getActualTypeArguments()[0])), field)
                            .toProvider((Provider)fieldValue);
                }
            }
            //noinspection unchecked
            return binder -> toAnnotatedBinding(binder.bind(TypeLiteral.get(fieldType)), field)
                    .toInstance(fieldValue);

        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    private static LinkedBindingBuilder toAnnotatedBinding(AnnotatedBindingBuilder builder, Field field) {
        return Arrays.stream(field.getAnnotations())
                .filter(a -> a.annotationType().isAnnotationPresent(Qualifier.class) || a.annotationType().isAnnotationPresent(BindingAnnotation.class))
                .findFirst()
                .map(builder::annotatedWith)
                .orElse(builder);
    }

    private static Stream<Field> getAllFields(Class cls) {
        return cls == Object.class
                ? Stream.empty()
                : Stream.concat(getAllFields(cls.getSuperclass()), Arrays.stream(cls.getDeclaredFields()));
    }
}
