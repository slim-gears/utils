package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleAllOptionalPropertiesConcreteWithCreator implements SampleAllOptionalPropertiesProto {

    @JsonCreator
    public static SampleAllOptionalPropertiesConcreteWithCreator create(
        @JsonProperty("intProperty") Integer intProperty,
        @JsonProperty("strProperty") String strProperty) {
        return new AutoValue_SampleAllOptionalPropertiesConcreteWithCreator(
            intProperty,
            strProperty);
    }

    public static SampleAllOptionalPropertiesConcreteWithCreator create() {
        return new AutoValue_SampleAllOptionalPropertiesConcreteWithCreator(
            null,
            null);
    }

    public static SampleAllOptionalPropertiesConcreteWithCreator ofIntProperty(Integer intProperty) {
        return create(
            intProperty,
            null);
    }

    public static SampleAllOptionalPropertiesConcreteWithCreator ofStrProperty(String strProperty) {
        return create(
            null,
            strProperty);
    }

    @Override @JsonProperty public abstract Integer intProperty();
    @Override @JsonProperty public abstract String strProperty();

}
