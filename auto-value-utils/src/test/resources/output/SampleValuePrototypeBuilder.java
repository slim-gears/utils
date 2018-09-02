package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleValuePrototypeBuilder<B extends SampleValuePrototypeBuilder<B>> {
    @JsonProperty("intValue")
    B intValue(int intValue);

    @SampleFieldAnnotation(strValue = "test")
    @JsonProperty("doubleValue")
    B doubleValue(double doubleValue);

    @SampleFieldAnnotation
    @JsonProperty("strValue")
    B strValue(String strValue);
}
