package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleComparableGenericPrototype<T extends Comparable<T>> extends com.slimgears.sample.SampleComparableGenericInterface<T> {
}
