package com.slimgears.util.test.guice;

import com.google.inject.Module;
import com.slimgears.util.guice.PropertyModules;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@ModuleProvider.Qualifier(UseProperties.Provider.class)
public @interface UseProperties {
    @Retention(RetentionPolicy.RUNTIME)
    @interface Property {
        String name();
        String value();
    }

    Property[] value();

    class Provider implements ModuleProvider<UseProperties> {
        @Override
        public Module createModule(UseProperties annotation, Method testMethod, Object target) {
            PropertyModules.Builder builder = PropertyModules.builder();
            Arrays.asList(annotation.value()).forEach(p -> builder.add(p.name(), p.value()));
            return builder.build();
        }
    }
}
