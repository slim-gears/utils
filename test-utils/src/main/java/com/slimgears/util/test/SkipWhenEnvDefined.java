package com.slimgears.util.test;

import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@IgnoreTestRule.Qualifier(SkipWhenEnvDefined.Provider.class)
public @interface SkipWhenEnvDefined {
    String value();

    class Provider implements IgnoreTestRule<SkipWhenEnvDefined> {
        @Override
        public boolean isIgnored(SkipWhenEnvDefined annotation, FrameworkMethod method) {
            return System.getenv(annotation.value()) != null;
        }
    }
}
