package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonAnyGetter;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype()
public interface SampleValuePrototype {
    int intValue();

    @SampleFieldAnnotation(strValue = "test")
    double doubleValue();

    @SampleFieldAnnotation
    String strValue();

    default String calculatedString() {
        return strValue() + doubleValue() + intValue();
    }
}
