package com.slimgears.sample.b;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.sample.a.SampleA;
import com.slimgears.sample.a.SampleAPrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleBPrototype {
    SampleA value();
    SampleAPrototype.NestedEnum nestedEnum();
}
