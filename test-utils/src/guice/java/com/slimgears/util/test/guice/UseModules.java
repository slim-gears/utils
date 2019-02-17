package com.slimgears.util.test.guice;

import com.google.inject.Module;
import com.google.inject.util.Modules;
import com.slimgears.util.stream.Safe;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@UseModules.Field
@UseProviders
@ModuleProvider.Qualifier(UseModules.Provider.class)
public @interface UseModules {
    Class<? extends Module>[] value() default {};

    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.TYPE})
    @ModuleProvider.Qualifier(UseModules.Field.Provider.class)
    @interface Field {
        class Provider implements ModuleProvider<Field> {
            @Override
            public Module createModule(Field annotation, FrameworkMethod testMethod, Object target) {
                return Arrays.stream(target.getClass().getDeclaredFields())
                        .filter(field -> field.isAnnotationPresent(UseModules.Field.class))
                        .filter(field -> Module.class.isAssignableFrom(field.getType()))
                        .map(field -> {
                            field.setAccessible(true);
                            try {
                                return (Module)field.get(target);
                            } catch (IllegalAccessException e) {
                                throw new RuntimeException(e);
                            }
                        })
                        .reduce(((module1, module2) -> Modules.override(module1).with(module2)))
                        .orElse(Modules.EMPTY_MODULE);
            }
        }
    }

    class Provider implements ModuleProvider<UseModules> {
        @Override
        public Module createModule(UseModules annotation, FrameworkMethod testMethod, Object target) {
            return Arrays.stream(annotation.value())
                    .<Module>map(Safe.ofFunction(Class::newInstance))
                    .reduce((m1, m2) -> Modules.override(m1).with(m2))
                    .orElse(Modules.EMPTY_MODULE);
        }
    }
}
