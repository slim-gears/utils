package com.slimgears.util.test;

import com.slimgears.util.generic.ServiceResolver;
import com.slimgears.util.generic.ServiceResolvers;
import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.function.Predicate;
import java.util.function.Supplier;

@SuppressWarnings("WeakerAccess")
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
                        annotatedItem -> toMethodRule(() -> resolver.resolve(annotatedItem.qualifier().value()), annotatedItem.annotation()))
                .reduce(MethodRules::combine)
                .orElse(empty)
                .apply(base, method, target);
    }

    public static boolean isIgnored(FrameworkMethod method) {
        return TestReflectUtils.providersForMethod(
                method,
                IgnoreTestRule.Qualifier.class,
                annotatedItem -> predicateForMethod(annotatedItem.annotation(), annotatedItem.qualifier().value()))
                .reduce(Predicate::and)
                .orElse(m -> false)
                .test(method);
    }

    @SuppressWarnings("unchecked")
    private static Predicate<FrameworkMethod> predicateForMethod(Annotation annotation, Class<? extends IgnoreTestRule<?>> ruleClass) {
        try {
            IgnoreTestRule rule = ruleClass.newInstance();
            return method -> rule.isIgnored(annotation, method);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static MethodRule annotationRule() {
        return annotationRule(ServiceResolvers.defaultConstructorResolver());
    }

    @SuppressWarnings("unchecked")
    private static MethodRule toMethodRule(Supplier<AnnotationMethodRule> rule, Annotation annotation) {
        return (base, method, target) -> rule.get().toMethodRule(annotation).apply(base, method, target);
    }
}
