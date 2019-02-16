package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@AutoValueExpressions
public interface SampleComparableGenericPrototype<T extends Comparable<T>> extends com.slimgears.sample.SampleComparableGenericInterface<T> {
}
