package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
public interface SampleComparableGenericPrototype<T extends Comparable<T>> extends com.slimgears.sample.SampleComparableGenericInterface<T> {
}
