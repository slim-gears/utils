package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleGenericPrototype<T> extends SampleInterface, SampleGenericInterface<T> {
}
