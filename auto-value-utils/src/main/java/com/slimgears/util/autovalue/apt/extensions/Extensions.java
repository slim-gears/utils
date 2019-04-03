package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.common.MoreTypes;
import com.slimgears.util.stream.Streams;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class Extensions {
    public static <E> Collection<E> fromStrings(Class<E> cls, String... qualifiedNames) {
        return fromStrings(cls, Arrays.asList(qualifiedNames));
    }

    public static <E> Collection<E> fromStrings(Class<E> cls, Iterable<String> qualifiedNames) {
        return Streams.fromIterable(qualifiedNames)
                .map(name -> fromString(cls, name))
                .collect(Collectors.toList());
    }

    public static <A extends Annotation> String[] namesFromType(TypeElement type, Class<A> annotationCls, Function<A, String[]> getter) {
        return type.getAnnotationMirrors()
                .stream()
                .flatMap(am -> namesFromAnnotationMirror(am, annotationCls, getter))
                .distinct()
                .toArray(String[]::new);
    }

    private static <A extends Annotation> Stream<String> namesFromAnnotationMirror(AnnotationMirror annotationMirror, Class<A> cls, Function<A, String[]> getter) {
        return Optional
                        .ofNullable(MoreTypes
                                .asTypeElement(annotationMirror.getAnnotationType())
                                .getAnnotation(cls))
                        .map(e -> Arrays.stream(getter.apply(e)))
                        .map(stream -> Stream.concat(stream, annotationMirror
                                .getAnnotationType()
                                .asElement()
                                .getAnnotationMirrors()
                                .stream()
                                .flatMap(am -> namesFromAnnotationMirror(am, cls, getter))))
                        .orElseGet(Stream::empty);
    }

    private static <E> E fromString(Class<E> extensionClass, String qualifiedName) {
        try {
            Class cls = Class.forName(qualifiedName);
            if (!extensionClass.isAssignableFrom(cls)) {
                throw new RuntimeException("Class " + qualifiedName + " is not a valid extension of type " + extensionClass.getSimpleName());
            }
            //noinspection unchecked
            return (E)cls.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
