package com.slimgears.util.test;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.lang.reflect.Method;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@IgnoreTestRule.Qualifier(SkipWhenEnvDefined.Provider.class)
public @interface SkipWhenEnvDefined {
    String value();

    class Provider implements IgnoreTestRule<SkipWhenEnvDefined> {
        @Override
        public boolean isIgnored(SkipWhenEnvDefined annotation, Method method) {
            return System.getenv(annotation.value()) != null;
        }
    }
}
