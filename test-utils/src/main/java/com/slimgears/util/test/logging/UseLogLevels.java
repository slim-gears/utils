package com.slimgears.util.test.logging;

import com.slimgears.util.test.AnnotationRuleProvider;
import com.slimgears.util.test.ExtensionRule;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.stream.Stream;

@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.METHOD, ElementType.TYPE})
@AnnotationRuleProvider.Qualifier(UseLogLevels.Provider.class)
public @interface UseLogLevels {
    UseLogLevel[] value();

    class Provider implements AnnotationRuleProvider<UseLogLevels> {
        @Override
        public ExtensionRule provide(UseLogLevels info) {
            return Stream.of(info.value())
                    .map(ll -> new UseLogLevel.Provider().provide(ll))
                    .reduce(ExtensionRule::andThen)
                    .orElse(ExtensionRule.empty());
        }
    }
}
