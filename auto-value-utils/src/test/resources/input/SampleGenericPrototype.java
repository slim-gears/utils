package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
public interface SampleGenericPrototype<T> extends SampleInterface, SampleGenericInterface<T> {
}
