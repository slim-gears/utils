package com.slimgears.util.test;

import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@IgnoreTestRule.Qualifier(SkipWhenEnvNotDefined.Provider.class)
public @interface SkipWhenEnvNotDefined {
    String[] value() default {};

    class Provider implements IgnoreTestRule<SkipWhenEnvNotDefined> {
        @Override
        public boolean isIgnored(SkipWhenEnvNotDefined annotation, FrameworkMethod method) {
            return Arrays.stream(annotation.value()).anyMatch(v -> System.getenv() == null);
        }
    }
}
