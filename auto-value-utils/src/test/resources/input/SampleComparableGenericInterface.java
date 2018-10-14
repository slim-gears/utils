package com.slimgears.sample;

public interface SampleComparableGenericInterface<T extends Comparable<T>> {
    T tValue();
}
