package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.reflect.TypeToken;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaBuilder;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.annotations.PropertyType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericAllOptionalPropertiesConcreteWithBuilder<T extends Comparable<T>> implements GenericAllOptionalPropertiesProto<T>, HasMetaClass<GenericAllOptionalPropertiesConcreteWithBuilder<T>> {

    @Override
    @JsonIgnore
    public MetaClass<GenericAllOptionalPropertiesConcreteWithBuilder<T>> metaClass() {
        return (MetaClass<GenericAllOptionalPropertiesConcreteWithBuilder<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T extends Comparable<T>> implements MetaClass<GenericAllOptionalPropertiesConcreteWithBuilder<T>> {
        private final TypeToken<GenericAllOptionalPropertiesConcreteWithBuilder<T>> objectType = new TypeToken<GenericAllOptionalPropertiesConcreteWithBuilder<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, Integer> intProperty = PropertyMeta.<GenericAllOptionalPropertiesConcreteWithBuilder<T>, Integer, Builder<T>>create(this, "intProperty", new PropertyType<Integer>(){}, obj -> obj.intProperty(), Builder::intProperty);
        public final PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, String> strProperty = PropertyMeta.<GenericAllOptionalPropertiesConcreteWithBuilder<T>, String, Builder<T>>create(this, "strProperty", new PropertyType<String>(){}, obj -> obj.strProperty(), Builder::strProperty);
        public final PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, T> genericValue = PropertyMeta.<GenericAllOptionalPropertiesConcreteWithBuilder<T>, T, Builder<T>>create(this, "genericValue", new PropertyType<T>(){}, obj -> obj.genericValue(), Builder::genericValue);

        Meta() {
            propertyMap.put("intProperty", intProperty);
            propertyMap.put("strProperty", strProperty);
            propertyMap.put("genericValue", genericValue);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<GenericAllOptionalPropertiesConcreteWithBuilder<T>> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, __V> getProperty(String name) {
            return (PropertyMeta<GenericAllOptionalPropertiesConcreteWithBuilder<T>, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<GenericAllOptionalPropertiesConcreteWithBuilder<T>>> B createBuilder() {
            return (B)(BuilderPrototype)GenericAllOptionalPropertiesConcreteWithBuilder.builder();
        }

        @Override
        public int hashCode() {
            return Objects.hash(objectType, builderClass);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Meta
            && Objects.equals(((Meta)obj).asType(), asType())
            && Objects.equals(((Meta)obj).builderClass(), builderClass());
        }
    }

    @JsonCreator
    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithBuilder<T> create(
        @Nullable @JsonProperty("intProperty") Integer intProperty,
        @Nullable @JsonProperty("strProperty") String strProperty,
        @Nullable @JsonProperty("genericValue") T genericValue) {
        return GenericAllOptionalPropertiesConcreteWithBuilder.<T>builder()
            .intProperty(intProperty)
            .strProperty(strProperty)
            .genericValue(genericValue)
            .build();
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithBuilder<T> create() {
        return GenericAllOptionalPropertiesConcreteWithBuilder.<T>builder()
            .build();
    }

    @JsonIgnore
    public abstract Builder<T> toBuilder();

    public static <T extends Comparable<T>> Builder<T> builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder<T extends Comparable<T>> extends BuilderPrototype<GenericAllOptionalPropertiesConcreteWithBuilder<T>, Builder<T>>, GenericAllOptionalPropertiesProtoBuilder<T, Builder<T>>{
        public static <T extends Comparable<T>> Builder<T> create() {
            return new AutoValue_GenericAllOptionalPropertiesConcreteWithBuilder.Builder<>();
        }

        @Override
        Builder<T> intProperty(Integer intProperty);

        @Override
        Builder<T> strProperty(String strProperty);

        @Override
        Builder<T> genericValue(T genericValue);
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithBuilder<T> fromIntProperty(Integer intProperty) {
        return GenericAllOptionalPropertiesConcreteWithBuilder.<T>builder().intProperty(intProperty).build();
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithBuilder<T> fromStrProperty(String strProperty) {
        return GenericAllOptionalPropertiesConcreteWithBuilder.<T>builder().strProperty(strProperty).build();
    }

    public static <T extends Comparable<T>> GenericAllOptionalPropertiesConcreteWithBuilder<T> fromGenericValue(T genericValue) {
        return GenericAllOptionalPropertiesConcreteWithBuilder.<T>builder().genericValue(genericValue).build();
    }

    @Override @Nullable @JsonProperty("intProperty") public abstract Integer intProperty();
    @Override @Nullable @JsonProperty("strProperty") public abstract String strProperty();
    @Override @Nullable @JsonProperty("genericValue") public abstract T genericValue();

}
