package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleAllOptionalPropertiesConcreteWithCreator implements SampleAllOptionalPropertiesProto {

    @JsonCreator
    public static SampleAllOptionalPropertiesConcreteWithCreator create(
        @Nullable @JsonProperty("intProperty") Integer intProperty,
        @Nullable @JsonProperty("strProperty") String strProperty) {
        return new AutoValue_SampleAllOptionalPropertiesConcreteWithCreator(
            intProperty,
            strProperty);
    }

    public static SampleAllOptionalPropertiesConcreteWithCreator create() {
        return new AutoValue_SampleAllOptionalPropertiesConcreteWithCreator(
            null,
            null);
    }

    public static SampleAllOptionalPropertiesConcreteWithCreator intProperty(Integer intProperty) {
        return create(
            intProperty,
            null);
    }

    public static SampleAllOptionalPropertiesConcreteWithCreator strProperty(String strProperty) {
        return create(
            null,
            strProperty);
    }

    @Override @Nullable @JsonProperty("intProperty") public abstract Integer intProperty();
    @Override @Nullable @JsonProperty("strProperty") public abstract String strProperty();

}
