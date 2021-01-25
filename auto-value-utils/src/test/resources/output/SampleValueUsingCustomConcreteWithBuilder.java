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
public abstract class SampleValueUsingCustomConcreteWithBuilder implements SampleValueUsingCustomProto, HasMetaClass<SampleValueUsingCustomConcreteWithBuilder> {

    @Override
    @JsonIgnore
    public MetaClass<SampleValueUsingCustomConcreteWithBuilder> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleValueUsingCustomConcreteWithBuilder> {
        private final TypeToken<SampleValueUsingCustomConcreteWithBuilder> objectType = new TypeToken<SampleValueUsingCustomConcreteWithBuilder>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleValueUsingCustomConcreteWithBuilder, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleValueUsingCustomConcreteWithBuilder, Integer> value = PropertyMeta.<SampleValueUsingCustomConcreteWithBuilder, Integer, Builder>create(this, "value", new PropertyType<Integer>(){}, obj -> obj.value(), Builder::value);
        public final PropertyMeta<SampleValueUsingCustomConcreteWithBuilder, Integer> optionalValue = PropertyMeta.<SampleValueUsingCustomConcreteWithBuilder, Integer, Builder>create(this, "optionalValue", new PropertyType<Integer>(){}, obj -> obj.optionalValue(), Builder::optionalValue);

        Meta() {
            propertyMap.put("value", value);
            propertyMap.put("optionalValue", optionalValue);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleValueUsingCustomConcreteWithBuilder> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleValueUsingCustomConcreteWithBuilder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleValueUsingCustomConcreteWithBuilder, __V> getProperty(String name) {
            return (PropertyMeta<SampleValueUsingCustomConcreteWithBuilder, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleValueUsingCustomConcreteWithBuilder>> B createBuilder() {
            return (B)(BuilderPrototype)SampleValueUsingCustomConcreteWithBuilder.builder();
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
    public static SampleValueUsingCustomConcreteWithBuilder create(
        @JsonProperty("value") int value,
        @Nullable @JsonProperty("optionalValue") Integer optionalValue) {
        return SampleValueUsingCustomConcreteWithBuilder.builder()
            .value(value)
            .optionalValue(optionalValue)
            .build();
    }

    public static SampleValueUsingCustomConcreteWithBuilder create(
        int value) {
        return SampleValueUsingCustomConcreteWithBuilder.builder()
            .value(value)
            .build();
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValueUsingCustomConcreteWithBuilder, Builder>, SampleValueUsingCustomProtoBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleValueUsingCustomConcreteWithBuilder.Builder();
        }

        @Override
        Builder value(int value);

        @Override
        Builder optionalValue(Integer optionalValue);
    }

    @Override @JsonProperty("value") public abstract int value();
    @Override @Nullable @JsonProperty("optionalValue") public abstract Integer optionalValue();

}
