package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleNestedTypePrototype {
    enum NestedEnum {
        Value1,
        Value2
    }

    NestedEnum value();
}
