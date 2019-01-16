package com.slimgears.utils.test.guice;

import com.google.inject.AbstractModule;
import com.google.inject.Guice;
import com.google.inject.Injector;
import com.google.inject.Module;
import com.google.inject.TypeLiteral;
import com.google.inject.util.Modules;
import com.slimgears.util.guice.GuiceServiceResolver;
import com.slimgears.utils.test.MethodRules;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.lang.reflect.Field;
import java.util.Arrays;
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
                Module module = Modules.override(GuiceMethodRule.this.module).with(ModuleProvider.forMethod(method));
                MockitoAnnotations.initMocks(target);

                module = Modules.override(module).with(createMockModule(target));
                Injector injector = Guice.createInjector(module);
                MethodRule annotationRule = MethodRules.annotationRule(GuiceServiceResolver.forInjector(injector));
                injector.injectMembers(target);

                annotationRule.apply(base, method, target).evaluate();
            }
        };
    }

    private Module createMockModule(Object target) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                getAllFields(target.getClass())
                        .filter(f -> f.isAnnotationPresent(Mock.class))
                        .forEach(f -> {
                            f.setAccessible(true);
                            TypeLiteral typeLiteral = TypeLiteral.get(f.getGenericType());
                            try {
                                //noinspection unchecked
                                bind(typeLiteral).toInstance(f.get(target));
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        });
            }
        };
    }

    private static Stream<Field> getAllFields(Class cls) {
        return cls == Object.class
                ? Stream.empty()
                : Stream.concat(getAllFields(cls.getSuperclass()), Arrays.stream(cls.getDeclaredFields()));
    }
}
