package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleValueUsingCustomConcrete implements SampleValueUsingCustomProto {

    @JsonCreator
    public static SampleValueUsingCustomConcrete create(
            @JsonProperty("optionalValue") Integer optionalValue,
            @JsonProperty("value") int value) {
        return new AutoValue_SampleValueUsingCustomConcrete(
                optionalValue,
                value);
    }

    public static SampleValueUsingCustomConcrete create(
            int value) {
        return new AutoValue_SampleValueUsingCustomConcrete(
                null,
                value);
    }

    @Override
    @Nullable
    @JsonProperty
    public abstract Integer optionalValue();

    @Override
    @JsonProperty
    public abstract int value();

}
