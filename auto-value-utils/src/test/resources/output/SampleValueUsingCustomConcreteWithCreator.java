package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleValueUsingCustomConcreteWithCreator implements SampleValueUsingCustomProto {

    @JsonCreator
    public static SampleValueUsingCustomConcreteWithCreator create(
        @JsonProperty("value") int value,
        @Nullable @JsonProperty("optionalValue") Integer optionalValue) {
        return new AutoValue_SampleValueUsingCustomConcreteWithCreator(
            value,
            optionalValue);
    }

    public static SampleValueUsingCustomConcreteWithCreator create(int value) {
        return new AutoValue_SampleValueUsingCustomConcreteWithCreator(
            value,
            null);
    }

    @Override @JsonProperty("value") public abstract int value();
    @Override @Nullable @JsonProperty("optionalValue") public abstract Integer optionalValue();

}
