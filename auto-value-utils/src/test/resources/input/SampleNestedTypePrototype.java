package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleNestedTypePrototype {
    enum NestedEnum {
        Value1,
        Value2
    }

    NestedEnum value();
}
