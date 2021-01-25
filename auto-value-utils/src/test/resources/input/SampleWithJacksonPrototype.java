package com.slimgears.sample;


import com.fasterxml.jackson.annotation.JsonProperty;

@CustomCompositeAnnotationWithJackson
public interface SampleWithJacksonPrototype {
    @JsonProperty("explicit_name_property")
    int explicitNameProperty();
}
