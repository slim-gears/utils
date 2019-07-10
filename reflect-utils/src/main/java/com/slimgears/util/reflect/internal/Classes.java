package com.slimgears.util.reflect.internal;

import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Stream;

public class Classes {
    public static Stream<Method> allMethods(Class<?> cls) {
        return streamMethods(cls, new HashSet<>(), new HashSet<>());
    }

    private static Stream<Method> streamMethods(Class<?> cls, Set<Class<?>> visitedInterfaces, Set<String> visitedMethods) {
        if (cls == null) {
            return Stream.empty();
        }

        Stream<Method> declaredMethods = Arrays
                .stream(cls.getDeclaredMethods())
                .filter(method -> visitedMethods.add(method.getName()));

        return cls == Object.class
                ? declaredMethods
                : Stream
                .of(
                        declaredMethods,
                        streamMethods(cls.getSuperclass(), visitedInterfaces, visitedMethods),
                        Arrays.stream(cls.getInterfaces())
                                .filter(visitedInterfaces::add)
                                .flatMap(i -> streamMethods(i, visitedInterfaces, visitedMethods)))
                .flatMap(Function.identity());
    }
}
