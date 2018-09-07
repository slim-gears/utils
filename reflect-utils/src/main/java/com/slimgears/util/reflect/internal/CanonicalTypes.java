package com.slimgears.util.reflect.internal;

import com.slimgears.util.reflect.CanonicalType;

import java.lang.reflect.GenericArrayType;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;

public class CanonicalTypes {
    public static Type toCanonical(Type type) {
        if (type instanceof CanonicalType) {
            return type;
        } else if (type instanceof Class) {
            Class cls = (Class)type;
            return cls.isArray()
                    ? new CanonicalGenericArrayType(toCanonical(cls.getComponentType()))
                    : type;
        } else if (type instanceof ParameterizedType) {
            return toCanonical((ParameterizedType)type);
        } else if (type instanceof GenericArrayType) {
            return toCanonical((GenericArrayType)type);
        } else if (type instanceof WildcardType) {
            return toCanonical((WildcardType)type);
        }
        return type;
    }

    public static Type toCanonical(ParameterizedType parameterizedType) {
        return new CanonicalParameterizedType(
                toCanonical(parameterizedType.getRawType()),
                toCanonical(parameterizedType.getOwnerType()),
                toCanonical(parameterizedType.getActualTypeArguments()));
    }

    public static Type toCanonical(GenericArrayType genericArrayType) {
        return new CanonicalGenericArrayType(toCanonical(genericArrayType.getGenericComponentType()));
    }

    public static Type toCanonical(WildcardType wildcardType) {
        return new CanonicalWildcardType(
                toCanonical(wildcardType.getLowerBounds()),
                toCanonical(wildcardType.getUpperBounds()));
    }

    public static Type[] toCanonical(Type[] types) {
        return Arrays.stream(types)
                .map(CanonicalTypes::toCanonical)
                .toArray(Type[]::new);
    }
}
