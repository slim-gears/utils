package com.slimgears.util.test;

import com.slimgears.util.stream.Safe;
import org.junit.runner.Description;
import org.junit.runners.model.FrameworkMethod;

import java.lang.annotation.Annotation;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.Optional;
import java.util.stream.Stream;

public class TestReflectUtils {
    public interface AnnotatedItem<A extends Annotation, Q extends Annotation> {
        A annotation();
        Q qualifier();
    }

    public interface ProviderFactory<T, Q extends Annotation> {
        T create(AnnotatedItem<Annotation, Q> annotatedItem) throws Exception;
    }

    public static <T, Q extends Annotation> Stream<T> providersForMethod(FrameworkMethod testMethod, Class<Q> qualifier, ProviderFactory<T, Q> factory) {
        return TestReflectUtils
                .getAnnotationsOf(testMethod.getMethod(), qualifier)
                .flatMap(item -> Stream.concat(Stream.of(item), TestReflectUtils.getAnnotationsOf(item.annotation().annotationType(), qualifier)))
                .map(Safe.ofFunction(factory::create));
    }

    public static <T, Q extends Annotation> Stream<T> providersForDescription(Description description, Class<Q> qualifier, ProviderFactory<T, Q> factory) {
        return TestReflectUtils
                .fromDescription(description, qualifier)
                .flatMap(item -> Stream.concat(Stream.of(item), TestReflectUtils.getAnnotationsOf(item.annotation().annotationType(), qualifier)))
                .map(Safe.ofFunction(factory::create));
    }

    public static <Q extends Annotation> Stream<AnnotatedItem<Annotation, Q>> fromDescription(Description description, Class<Q> annotationQualifier) {
        return description.getAnnotations()
                .stream()
                .flatMap(a -> getAnnotation(a, annotationQualifier));
    }

    public static <Q extends Annotation> Stream<AnnotatedItem<Annotation, Q>> getAnnotationsOf(AnnotatedElement element, Class<Q> annotationQualifier) {
        Stream<AnnotatedItem<Annotation, Q>> stream = Arrays
                .stream(element.getAnnotations())
                .flatMap(a -> getAnnotation(a, annotationQualifier));

        return (element instanceof Method)
                ? Stream.concat(
                TestReflectUtils.getAnnotationsOf(((Method) element).getDeclaringClass(), annotationQualifier),
                stream)
                : stream;
    }

    private static <A extends Annotation, Q extends Annotation> Stream<AnnotatedItem<A, Q>> getAnnotation(Annotation annotation, Class<Q> qualifier) {
        return Optional
                .of(annotation.annotationType())
                .filter(at -> at.isAnnotationPresent(qualifier))
                .map(at -> at.getAnnotation(qualifier))
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .map(q -> item(annotation, q));
    }

    private static <A extends Annotation, Q extends Annotation> AnnotatedItem<A, Q> item(Annotation annotation, Q qualifier) {
        return new AnnotatedItem<A, Q>() {
            @Override
            public A annotation() {
                //noinspection unchecked
                return (A)annotation;
            }

            @Override
            public Q qualifier() {
                return qualifier;
            }
        };
    }
}
