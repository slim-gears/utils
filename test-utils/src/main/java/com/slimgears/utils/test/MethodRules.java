package com.slimgears.utils.test;

import com.slimgears.util.generic.ServiceResolver;
import com.slimgears.util.generic.ServiceResolvers;
import org.junit.rules.MethodRule;

import java.lang.annotation.Annotation;
import java.util.Arrays;

public class MethodRules {
    public final static MethodRule empty = (base, method, target) -> base;

    public static MethodRule combine(MethodRule first, MethodRule second) {
        return (base, method, target) -> first.apply(second.apply(base, method, target), method, target);
    }

    public static MethodRule combine(MethodRule... rules) {
        return Arrays.stream(rules).reduce(MethodRules::combine).orElse(empty);
    }

    public static MethodRule annotationRule(ServiceResolver resolver) {
        return (base, method, target) -> TestReflectUtils
                .providersForMethod(
                        method,
                        AnnotationMethodRule.Qualifier.class,
                        annotatedItem -> toMethodRule(resolver.resolve(annotatedItem.qualifier().value()), annotatedItem.annotation()))
                .reduce(MethodRules::combine)
                .orElse(empty)
                .apply(base, method, target);
    }

    public static MethodRule annotationRule() {
        return annotationRule(ServiceResolvers.defaultConstructorResolver());
    }

    private static MethodRule toMethodRule(AnnotationMethodRule rule, Annotation annotation) {
        //noinspection unchecked
        return rule.toMethodRule(annotation);
    }
}
