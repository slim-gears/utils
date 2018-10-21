package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleValue.Builder.class)
@JsonSerialize(as = SampleValue.class)
public abstract class SampleValue implements SampleValuePrototype {
    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @Override
    public abstract int intValue();

    @Override
    @SampleFieldAnnotation(strValue = "test")
    public abstract double doubleValue();

    @Override
    @SampleFieldAnnotation
    @Nullable
    public abstract String strValue();

    @Override
    public abstract boolean foo();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValue, Builder>, SampleValuePrototypeBuilder<Builder> {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_SampleValue.Builder();
        }

        @Override
        Builder intValue(int intValue);

        @Override
        @SampleFieldAnnotation(strValue = "test")
        Builder doubleValue(double doubleValue);

        @Override
        @SampleFieldAnnotation
        Builder strValue(String strValue);

        @Override
        Builder foo(boolean foo);
    }
}
