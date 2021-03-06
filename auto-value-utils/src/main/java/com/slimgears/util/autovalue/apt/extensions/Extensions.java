package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.common.MoreTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMultimap;
import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.util.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.TypeElement;
import java.lang.annotation.Annotation;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.Optional;
import java.util.ServiceLoader;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class Extensions {
    private final static Logger log = LoggerFactory.getLogger(Extensions.class);

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

    public static <E> ImmutableMultimap<String, E> loadExtensions(Class<E> extensionClass) {
        ImmutableMultimap.Builder<String, E> builder = ImmutableMultimap.builder();
        Streams.fromIterable(ServiceLoader.load(extensionClass, Extensions.class.getClassLoader()))
                .forEach(e -> supportedAnnotations(e.getClass())
                    .peek(a -> log.debug("Found supported extension for {}: {}", a, e.getClass().getSimpleName()))
                    .forEach(a -> builder.put(a, e)));
        return builder.build();
    }

    public static <E> Collection<E> combine(Collection<E> list, E item) {
        return combine(list, ImmutableList.of(item));
    }

    public static <E> Collection<E> combine(Collection<E> list, Collection<E> other) {
        Set<Class<?>> classes = list.stream()
            .map(Object::getClass)
            .collect(Collectors.toSet());
        Collection<E> filteredOther = other
            .stream()
            .filter(i -> !classes.contains(i.getClass()))
            .collect(Collectors.toList());
        return filteredOther.isEmpty()
            ? list
            : ImmutableList.<E>builder()
            .addAll(list)
            .addAll(filteredOther)
            .build();
    }

    public static <E> Collection<E> extensionsForType(ImmutableMultimap<String, E> extensionMap, TypeElement type) {
        return type.getAnnotationMirrors().stream()
                .flatMap(am -> extensionsForAnnotationMirror(extensionMap, new HashSet<>(), am))
                .distinct()
                .peek(e -> log.debug("Found extension for {}: {}", type.getSimpleName(), e.getClass().getSimpleName()))
                .collect(Collectors.toList());
    }

    public static <E> Collection<E> extensionsForType(ImmutableMultimap<String, E> extensionMap, Class<?> type) {
        return Arrays.stream(type.getAnnotations())
                .flatMap(am -> extensionsForAnnotation(extensionMap, new HashSet<>(), am))
                .distinct()
                .peek(e -> log.debug("Found extension for {}: {}", type.getSimpleName(), e.getClass().getSimpleName()))
                .collect(Collectors.toList());
    }

    private static <E> Stream<E> extensionsForAnnotationMirror(ImmutableMultimap<String, E> extensionMap, Set<String> visitedAnnotations, AnnotationMirror annotationMirror) {
        String annotationTypeName = annotationMirror.getAnnotationType().toString();
        log.debug("Looking for extensions for annotation type: {}", annotationMirror.getAnnotationType().asElement().getSimpleName());
        return visitedAnnotations.add(annotationTypeName)
            ? Stream.concat(
                    Optional.ofNullable(extensionMap.get(annotationTypeName))
                            .map(Collection::stream)
                            .orElseGet(Stream::empty),
                    annotationMirror.getAnnotationType()
                            .asElement()
                            .getAnnotationMirrors()
                            .stream()
                            .flatMap(am -> extensionsForAnnotationMirror(extensionMap, visitedAnnotations, am)))
            : Stream.empty();
    }

    private static <E> Stream<E> extensionsForAnnotation(ImmutableMultimap<String, E> extensionMap, Set<String> visitedAnnotations, Annotation annotation) {
        String annotationTypeName = annotation.annotationType().getName();
        log.debug("Looking for extensions for annotation type: {}", annotation.annotationType().getSimpleName());
        return visitedAnnotations.add(annotationTypeName)
            ? Stream.concat(
                    Optional.ofNullable(extensionMap.get(annotationTypeName))
                            .map(Collection::stream)
                            .orElseGet(Stream::empty),
                    Arrays.stream(annotation.annotationType()
                            .getAnnotations())
                            .flatMap(am -> extensionsForAnnotation(extensionMap, visitedAnnotations, am)))
            : Stream.empty();
    }

    public static Collection<AnnotationInfo> allAnnotationsForType(TypeElement typeElement) {
        return typeElement.getAnnotationMirrors().stream()
                .flatMap(am -> allAnnotationsForAnnotationMirror(new HashSet<>(), am))
                .distinct()
                .collect(Collectors.toList());
    }

    private static Stream<AnnotationInfo> allAnnotationsForAnnotationMirror(Set<String> visitedAnnotations, AnnotationMirror annotationMirror) {
        String annotationTypeName = annotationMirror.getAnnotationType().toString();
        return visitedAnnotations.add(annotationTypeName)
                ? Stream.concat(
                        Stream.of(AnnotationInfo.of(annotationMirror)),
                        annotationMirror.getAnnotationType()
                                .asElement()
                                .getAnnotationMirrors()
                                .stream()
                                .flatMap(am -> allAnnotationsForAnnotationMirror(visitedAnnotations, am)))
                : Stream.empty();
    }

    private static Stream<String> supportedAnnotations(Class<?> cls) {
        return Stream.concat(
                Optional.ofNullable(cls.getAnnotation(SupportedAnnotationTypes.class))
                        .map(SupportedAnnotationTypes::value)
                        .map(Arrays::stream)
                        .orElseGet(Stream::empty),
                Optional.ofNullable(cls.getAnnotation(SupportedAnnotations.class))
                        .map(SupportedAnnotations::value)
                        .map(Arrays::stream)
                        .orElseGet(Stream::empty)
                        .map(Class::getName));
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

    @SuppressWarnings("unchecked")
    private static <E> E fromString(Class<E> extensionClass, String qualifiedName) {
        try {
            Class cls = Class.forName(qualifiedName);
            if (!extensionClass.isAssignableFrom(cls)) {
                throw new RuntimeException("Class " + qualifiedName + " is not a valid extension of type " + extensionClass.getSimpleName());
            }
            return (E)cls.newInstance();
        } catch (ClassNotFoundException | IllegalAccessException | InstantiationException e) {
            throw new RuntimeException(e);
        }
    }

}
