package com.slimgears.utils.test.guice;

import com.google.inject.Module;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.ANNOTATION_TYPE, ElementType.TYPE, ElementType.METHOD})
@ModuleProvider.Qualifier(UseProviders.Provider.class)
public @interface UseProviders {
    @Retention(RetentionPolicy.RUNTIME)
    @Target({ElementType.FIELD, ElementType.METHOD})
    @interface Binding {

    }

    class Provider implements ModuleProvider<UseProviders> {
        @Override
        public Module createModule(UseProviders annotation, FrameworkMethod testMethod, Object target) {
            return GuiceMethodRule.createModuleFromFields(target, Binding.class);
        }
    }
}
