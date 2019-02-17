package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleValuePrototype {
    int intValue();

    @SampleFieldAnnotation(strValue = "test")
    double doubleValue();

    @SampleFieldAnnotation
    @Nullable
    String strValue();

    boolean foo();

    default String calculatedString() {
        return strValue() + doubleValue() + intValue();
    }
}
