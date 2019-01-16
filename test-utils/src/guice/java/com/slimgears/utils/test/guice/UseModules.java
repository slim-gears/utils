package com.slimgears.utils.test.guice;

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
@ModuleProvider.Qualifier(UseModules.Provider.class)
public @interface UseModules {
    Class<? extends Module>[] value() default {};

    class Provider implements ModuleProvider<UseModules> {
        @Override
        public Module createModule(UseModules annotation, FrameworkMethod testMethod) {
            return Arrays.stream(annotation.value())
                    .<Module>map(Safe.ofFunction(Class::newInstance))
                    .reduce((m1, m2) -> Modules.override(m1).with(m2))
                    .orElse(Modules.EMPTY_MODULE);
        }
    }
}
