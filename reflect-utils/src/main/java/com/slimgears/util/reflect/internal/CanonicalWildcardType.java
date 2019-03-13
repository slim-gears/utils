package com.slimgears.util.reflect.internal;

import com.slimgears.util.reflect.CanonicalType;

import java.lang.reflect.Type;
import java.lang.reflect.WildcardType;
import java.util.Arrays;
import java.util.Objects;
import java.util.stream.Collectors;

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
        return "?" + boundsToString(" extends ", upperBounds) + boundsToString(" super ", lowerBounds);
    }

    private String boundsToString(String prefix, Type[] bounds) {

        return (bounds.length > 0) && !(bounds.length == 1 && Object.class.equals(bounds[0]))
                ? Arrays.stream(bounds)
                    .map(Object::toString).collect(Collectors.joining(" & ", prefix, ""))
                : "";
    }

    @Override
    public Class<?> asClass() {
        return null;
    }
}
