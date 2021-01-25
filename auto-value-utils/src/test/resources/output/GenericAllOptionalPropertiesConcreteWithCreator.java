package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericAllOptionalPropertiesConcreteWithCreator<T extends Comparable<T>> implements GenericAllOptionalPropertiesProto<T> {

    @JsonCreator
    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithCreator<T> create(
        @Nullable @JsonProperty("intProperty") Integer intProperty,
        @Nullable @JsonProperty("strProperty") String strProperty,
        @Nullable @JsonProperty("genericValue") T genericValue) {
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

    @Override @Nullable @JsonProperty("intProperty") public abstract Integer intProperty();
    @Override @Nullable @JsonProperty("strProperty") public abstract String strProperty();
    @Override @Nullable @JsonProperty("genericValue") public abstract T genericValue();

}
