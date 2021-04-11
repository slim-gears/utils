package com.slimgears.util.test.logging;

import com.slimgears.util.test.AnnotationRuleProvider;
import com.slimgears.util.test.ExtensionRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@AnnotationRuleProvider.Qualifier(UseLogLevel.Provider.class)
public @interface UseLogLevel {

    LogLevel value();
    String logger() default "";

    class Provider implements AnnotationRuleProvider<UseLogLevel> {
        @Override
        public ExtensionRule provide(UseLogLevel info) {
            return (method, target) -> LogLevelListener.apply(info.logger(), info.value())::apply;
        }
    }
}
