package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleGenericInterfaceBuilder<T, B extends SampleGenericInterfaceBuilder<T, B>> {
    @JsonProperty("tValue")
    B tValue(T tValue);
}
