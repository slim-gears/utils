package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleComparableGeneric.Builder.class)
@JsonSerialize(as = SampleComparableGeneric.class)
public abstract class SampleComparableGeneric<T extends Comparable<T>> implements SampleComparableGenericPrototype<T> {
    @JsonIgnore
    public abstract Builder<T> toBuilder();

    public static <T extends Comparable<T>> Builder<T> builder() {
        return Builder.create();
    }

    @Override
    public abstract T tValue();

    @AutoValue.Builder
    public interface Builder<T extends Comparable<T>> extends BuilderPrototype<SampleComparableGeneric<T>, Builder<T>>, SampleComparableGenericPrototypeBuilder<T, Builder<T>> {
        @JsonCreator
        public static <T extends Comparable<T>> Builder<T> create() {
            return new AutoValue_SampleComparableGeneric.Builder<>();
        }

        @Override
        Builder<T> tValue(T tValue);
    }
}
