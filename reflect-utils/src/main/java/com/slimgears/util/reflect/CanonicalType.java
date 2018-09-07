package com.slimgears.util.reflect;

import com.slimgears.util.reflect.internal.CanonicalTypes;

import java.lang.reflect.Type;

public interface CanonicalType extends Type {
    Class<?> asClass();

    static Type of(Type type) {
        return CanonicalTypes.toCanonical(type);
    }
}
