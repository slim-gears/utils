package com.slimgears.util.reflect;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ReflectUtils {
    private final static Map<Class<?>, Collection<Class<?>>> classHierarchyCache = new HashMap<>();
    private final static Map<String, Collection<Method>> methodHierarchyCache = new HashMap<>();

    public static Stream<Method> allMethods(Class<?> cls) {
        return streamMethods(cls, new HashSet<>());
    }

    public static Stream<Method> allMethods(Class<?> cls, String name, Class<?>... args) {
        return methodHierarchy(cls, name, args);
    }

    public static Optional<Method> findMethod(Class<?> cls, String name, Class<?>... args) {
        try {
            return Optional.of(cls.getDeclaredMethod(name, args));
        } catch (NoSuchMethodException e) {
            return Optional.empty();
        }
    }

    public static Stream<Class<?>> classHierarchy(Class<?> cls) {
        if (classHierarchyCache.containsKey(cls)) {
            return classHierarchyCache.get(cls).stream();
        }

        var set = new HashSet<Class<?>>();
        return streamClasses(cls, set).onClose(() -> {
            synchronized (classHierarchyCache) {
                classHierarchyCache.put(cls, set);
            }
        });
    }

    public static Stream<Annotation> allAnnotations(Class<?> cls) {
        return classHierarchy(cls).flatMap(c -> Arrays.stream(c.getAnnotations()));
    }

    public static <A extends Annotation> Stream<A> allAnnotations(Class<?> cls, Class<A> annotationType) {
        return classHierarchy(cls).map(c -> c.getAnnotation(annotationType));
    }

    public static Stream<Method> methodHierarchy(Class<?> cls, String name, Class<?>... args) {
        var key = methodKey(cls, name, args);
        if (!methodHierarchyCache.containsKey(key)) {
            var methods = classHierarchy(cls)
                    .flatMap(c -> findMethod(c, name, args).stream())
                    .collect(Collectors.toList());

            synchronized (methodHierarchyCache) {
                methodHierarchyCache.put(key, methods);
            }
        }
        return methodHierarchyCache.get(key).stream();
    }

    private static Stream<Class<?>> streamClasses(Class<?> cls, Set<Class<?>> visitedClasses) {
        if (cls == null || !visitedClasses.add(cls)) {
            return Stream.empty();
        }

        return cls == Object.class
                ? Stream.of(cls)
                : Stream.of(
                        Stream.of(cls),
                        streamClasses(cls.getSuperclass(), visitedClasses),
                        Arrays.stream(cls.getInterfaces()).flatMap(i -> streamClasses(i, visitedClasses)))
                .flatMap(Function.identity());
    }

    private static Stream<Method> streamMethods(Class<?> cls, Set<Class<?>> visitedInterfaces) {
        return streamClasses(cls, visitedInterfaces)
                .flatMap(c -> Arrays.stream(c.getDeclaredMethods()))
                .distinct();
    }

    private static String methodKey(Class<?> cls, String name, Class<?>... args) {
        return cls.getName() + "." + name + Arrays.stream(args).map(Class::getName).collect(Collectors.joining(",", "(", ")"));
    }
}
