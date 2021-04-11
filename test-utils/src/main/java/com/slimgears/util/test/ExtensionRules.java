package com.slimgears.util.test;

import com.slimgears.util.generic.ServiceResolver;
import com.slimgears.util.generic.ServiceResolvers;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.function.Supplier;
import java.util.stream.StreamSupport;

@SuppressWarnings("WeakerAccess")
public class ExtensionRules {
    public final static ExtensionRule empty = ExtensionRule.empty();

    public static ExtensionRule combine(ExtensionRule... rules) {
        return Arrays.stream(rules).reduce(ExtensionRule::andThen).orElse(empty);
    }

    public static ExtensionRule annotationRule(ServiceResolver resolver) {
        return (method, target) -> TestReflectUtils.providersForMethod(
                method,
                AnnotationRuleProvider.Qualifier.class,
                annotatedItem -> toExtensionRule(() -> resolver.resolve(annotatedItem.qualifier().value()), annotatedItem.annotation()))
                .reduce(ExtensionRules::combine)
                .orElse(empty)
                .prepare(method, target);
    }

    public static boolean isIgnored(Method method) {
        return TestReflectUtils.providersForMethod(
                method,
                IgnoreTestRule.Qualifier.class,
                annotatedItem -> predicateForMethod(annotatedItem.annotation(), annotatedItem.qualifier().value()))
                .reduce(Predicate::and)
                .orElse(m -> false)
                .test(method);
    }

    @SuppressWarnings("unchecked")
    private static Predicate<Method> predicateForMethod(Annotation annotation, Class<? extends IgnoreTestRule<?>> ruleClass) {
        try {
            IgnoreTestRule rule = ruleClass.newInstance();
            return method -> rule.isIgnored(annotation, method);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static ExtensionRule annotationRule() {
        return annotationRule(ServiceResolvers.defaultConstructorResolver());
    }

    public static ExtensionRule discover() {
        return StreamSupport
                .stream(ServiceLoader.load(ExtensionRule.class, ExtensionRule.class.getClassLoader()).spliterator(), false)
                .reduce(ExtensionRule::andThen)
                .orElseGet(ExtensionRule::empty);
    }

    @SuppressWarnings("unchecked")
    private static ExtensionRule toExtensionRule(Supplier<AnnotationRuleProvider> rule, Annotation annotation) {
        return rule.get().provide(annotation);
    }
}
