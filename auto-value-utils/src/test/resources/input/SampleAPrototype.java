package com.slimgears.sample.a;

import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
@UseMetaDataExtension
public interface SampleAPrototype {
    enum NestedEnum {
        EnumVal1,
        EnumVal2
    }
    int value();
}
