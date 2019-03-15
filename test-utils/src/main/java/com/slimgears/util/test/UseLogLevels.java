package com.slimgears.util.test;

import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@AnnotationMethodRule.Qualifier(UseLogLevels.Provider.class)
public @interface UseLogLevels {
    UseLogLevel[] value();

    class Provider implements AnnotationMethodRule<UseLogLevels> {
        @Override
        public Statement apply(UseLogLevels info, Statement base, FrameworkMethod method, Object target) {
            UseLogLevel.Provider provider = new UseLogLevel.Provider();
            for (UseLogLevel logLevel : info.value()) {
                base = provider.apply(logLevel, base, method, target);
            }
            return base;
        }
    }
}
