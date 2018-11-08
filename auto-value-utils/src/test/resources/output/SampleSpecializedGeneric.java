package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleSpecializedGeneric.Builder.class)
@JsonSerialize(as = SampleSpecializedGeneric.class)
public abstract class SampleSpecializedGeneric implements SampleSpecializedGenericPrototype {
    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @Override
    public abstract ImmutableList<String> values();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleSpecializedGeneric, Builder>, SampleSpecializedGenericPrototypeBuilder<Builder> {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_SampleSpecializedGeneric.Builder();
        }

        @Override
        Builder values(ImmutableList<String> values);
        ImmutableList.Builder<String> valuesBuilder();

    }
}
