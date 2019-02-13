package com.slimgears.util.reflect.internal;

import com.slimgears.util.reflect.CanonicalType;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;

public class CanonicalWildcardType implements WildcardType, CanonicalType {
    private final Type[] lowerBounds;
    private final Type[] upperBounds;

    public CanonicalWildcardType(Type[] lowerBounds, Type[] upperBounds) {
        this.lowerBounds = lowerBounds;
        this.upperBounds = upperBounds;
    }

    @Override
    public Type[] getUpperBounds() {
        return upperBounds;
    }

    @Override
    public Type[] getLowerBounds() {
        return lowerBounds;
    }

    @Override
    public int hashCode() {
        return Objects.hash(Arrays.hashCode(lowerBounds), Arrays.hashCode(upperBounds));
    }

    @Override
    public boolean equals(Object obj) {
        return obj instanceof CanonicalWildcardType &&
                Arrays.equals(lowerBounds, ((CanonicalWildcardType) obj).lowerBounds) &&
                Arrays.equals(upperBounds, ((CanonicalWildcardType) obj).upperBounds);
    }

    @Override
    public String toString() {
        return "?";
    }

    @Override
    public Class<?> asClass() {
        return null;
    }
}
