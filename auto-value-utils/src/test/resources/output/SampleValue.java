package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import javax.annotation.Generated;


@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleValue.Builder.class)
@JsonSerialize(as = SampleValue.class)
public abstract class SampleValue {

    @JsonProperty("intValue")
    public abstract int intValue();

    @SampleFieldAnnotation(strValue = "test")
    @JsonProperty("doubleValue")
    public abstract double doubleValue();

    @SampleFieldAnnotation
    @JsonProperty("strValue")
    public abstract String strValue();

    public abstract Builder toBuilder();

    public static  Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValue, Builder> {

        @JsonProperty("intValue")
        Builder intValue(int intValue);

        @SampleFieldAnnotation(strValue = "test")
        @JsonProperty("doubleValue")
        Builder doubleValue(double doubleValue);

        @SampleFieldAnnotation
        @JsonProperty("strValue")
        Builder strValue(String strValue);

        @JsonCreator
        public static  Builder create() {
            return new AutoValue_SampleValue.Builder();
        }
    }
}
