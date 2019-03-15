package com.slimgears.util.test;

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
    enum Level {
        OFF,
        SEVERE,
        WARNING,
        INFO,
        CONFIG,
        FINE,
        FINER,
        FINEST
    }

    Level value();
    String logger() default "";

    class Provider implements AnnotationMethodRule<UseLogLevel> {
        @Override
        public Statement apply(UseLogLevel info, Statement base, FrameworkMethod method, Object target) {
            return new Statement() {
                @Override
                public void evaluate() throws Throwable {
                    Logger logger = Logger.getLogger(info.logger());
                    java.util.logging.Level level = java.util.logging.Level.parse(info.value().name());
                    java.util.logging.Level prevLevel = logger.getLevel();
                    Map<Handler, java.util.logging.Level> handlerLevelMap = new HashMap<>();
                    logger.setLevel(level);
                    Arrays.asList(logger.getHandlers()).forEach(h -> {
                        handlerLevelMap.put(h, h.getLevel());
                        h.setLevel(level);
                    });
                    try {
                        base.evaluate();
                    } finally {
                        handlerLevelMap.forEach(Handler::setLevel);
                        logger.setLevel(prevLevel);
                    }
                }
            };
        }
    }
}
