package com.slimgears.util.test.logging;

import com.slimgears.util.test.AnnotationMethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Handler;
import java.util.logging.Logger;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@AnnotationMethodRule.Qualifier(UseLogLevel.Provider.class)
public @interface UseLogLevel {

    LogLevel value();
    String logger() default "";

    class Provider implements AnnotationMethodRule<UseLogLevel> {
        @Override
        public Statement apply(UseLogLevel info, Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    LogLevelListener.Rollback rollback = LogLevelListener.apply(info.logger(), info.value());
                    try {
                        base.evaluate();
                    } finally {
                        rollback.apply();
                    }
                }
            };
        }
    }
}
