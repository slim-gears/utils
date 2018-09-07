package com.slimgears.util.reflect.internal;

import com.slimgears.util.reflect.CanonicalType;

import java.lang.reflect.Array;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.Type;
import java.util.Objects;

public class CanonicalGenericArrayType implements GenericArrayType, CanonicalType {
    private final Type componentType;

    public CanonicalGenericArrayType(Type componentType) {
        this.componentType = componentType;
    }

    @Override
    public Type getGenericComponentType() {
        return componentType;
    }

    @Override
    public String toString() {
        return componentType.getTypeName() + "[]";
    }

    @Override
    public int hashCode() {
        return componentType.hashCode();
    }

    @Override
    public boolean equals(Object obj) {
        return (obj instanceof CanonicalGenericArrayType) &&
                Objects.equals(componentType, ((CanonicalGenericArrayType) obj).componentType);
    }

    @Override
    public Class<?> asClass() {
        return Array
                .newInstance(CanonicalTypes.toClass(componentType), 0)
                .getClass();
    }
}
