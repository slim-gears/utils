package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericAllOptionalPropertiesConcreteWithCreator<T extends Comparable<T>> implements GenericAllOptionalPropertiesProto<T> {

    @JsonCreator
    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithCreator<T> create(
        @JsonProperty("intProperty") Integer intProperty,
        @JsonProperty("strProperty") String strProperty,
        @JsonProperty("genericValue") T genericValue) {
        return new AutoValue_GenericAllOptionalPropertiesConcreteWithCreator<>(
            intProperty,
            strProperty,
            genericValue);
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithCreator<T> create() {
        return new AutoValue_GenericAllOptionalPropertiesConcreteWithCreator<>(
            null,
            null,
            null);
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithCreator<T> intProperty(Integer intProperty) {
        return create(
            intProperty,
            null,
            null);
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithCreator<T> strProperty(String strProperty) {
        return create(
            null,
            strProperty,
            null);
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithCreator<T> genericValue(T genericValue) {
        return create(
            null,
            null,
            genericValue);
    }

    @Override @JsonProperty("intProperty") public abstract Integer intProperty();
    @Override @JsonProperty("strProperty") public abstract String strProperty();
    @Override @JsonProperty("genericValue") public abstract T genericValue();

}
