package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueExpressions;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@AutoValueExpressions
public interface SampleGenericPrototype<T> extends SampleInterface, SampleGenericInterface<T> {
}
