package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseCreatorMethods;
import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@UseMetaDataExtension
@UseCreatorMethods
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

    static SampleValue createDefault() {
        return SampleValue.builder().build();
    }
}
