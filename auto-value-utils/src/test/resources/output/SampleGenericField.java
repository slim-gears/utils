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
@JsonDeserialize(builder = SampleGenericField.Builder.class)
@JsonSerialize(as = SampleGenericField.class)
public abstract class SampleGenericField implements SampleGenericFieldPrototype{
    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleGenericField, Builder>, SampleGenericFieldPrototypeBuilder<Builder> {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_SampleGenericField.Builder();
        }
    }
}
