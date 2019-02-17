package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleComparableGenericPrototype<T extends Comparable<T>> extends com.slimgears.sample.SampleComparableGenericInterface<T> {
}
