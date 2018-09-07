package com.slimgears.util.reflect.internal;

import com.slimgears.util.reflect.CanonicalType;

import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

public class CanonicalParameterizedType implements ParameterizedType, CanonicalType {
    private final Type[] args;
    private final Type rawType;
    private final Type ownerType;

    public CanonicalParameterizedType(Type rawType, Type ownerType, Type... args) {
        this.args = args;
        this.rawType = rawType;
        this.ownerType = ownerType;
    }

    @Override
    public Type[] getActualTypeArguments() {
        return args;
    }

    @Override
    public Type getRawType() {
        return rawType;
    }

    @Override
    public Type getOwnerType() {
        return ownerType;
    }

    @Override
    public String toString() {
        return rawType.getTypeName() + Arrays.stream(args)
                .map(Type::getTypeName)
                .collect(Collectors.joining(",", "<", ">"));
    }

    @Override
    public int hashCode() {
        return Objects.hash(rawType, ownerType, Arrays.hashCode(args));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CanonicalParameterizedType &&
                Objects.equals(rawType, ((CanonicalParameterizedType) obj).rawType) &&
                Objects.equals(ownerType, ((CanonicalParameterizedType) obj).ownerType) &&
                Arrays.equals(args, ((CanonicalParameterizedType) obj).args);
    }
}
