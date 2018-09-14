package com.slimgears.util.stream;

import java.util.Comparator;

public class Comparables {
    public static <T extends Comparable<T>> T max(T first, T second) {
        return Comparator.<T>nullsFirst(Comparator.naturalOrder()).compare(first, second) > 0 ? first : second;
    }

    public static <T extends Comparable<T>> T min(T first, T second) {
        return Comparator.<T>nullsLast(Comparator.naturalOrder()).compare(first, second) < 0 ? first : second;
    }
}
