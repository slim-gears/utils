package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonProperty;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleInterfaceBuilder<B extends SampleInterfaceBuilder<B>> {
    @JsonProperty("value")
    B value(String value);
}
