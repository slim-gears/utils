package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleWithJackson implements SampleWithJacksonPrototype {

    @JsonCreator
    public static SampleWithJackson create(
        @JsonProperty("explicit_name_property") int explicitNameProperty) {
        return SampleWithJackson.builder()
            .explicitNameProperty(explicitNameProperty)
            .build();
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleWithJackson, Builder>, SampleWithJacksonPrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleWithJackson.Builder();
        }

        @Override
        @JsonProperty("explicit_name_property")
        Builder explicitNameProperty(int explicitNameProperty);
    }

    @Override @JsonProperty("explicit_name_property") public abstract int explicitNameProperty();

}
