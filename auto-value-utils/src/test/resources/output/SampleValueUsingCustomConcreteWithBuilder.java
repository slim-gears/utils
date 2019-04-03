package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleValueUsingCustomConcrete implements SampleValueUsingCustomProto, HasMetaClass<com.slimgears.sample.SampleValueUsingCustomConcrete> {

    @Override
    public MetaClass<SampleValueUsingCustomConcrete> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleValueUsingCustomConcrete> {
        private final TypeToken<SampleValueUsingCustomConcrete> objectClass = new TypeToken<SampleValueUsingCustomConcrete>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleValueUsingCustomConcrete, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleValueUsingCustomConcrete, Integer> optionalValue = PropertyMeta.<SampleValueUsingCustomConcrete, Integer, Builder>create(this, "optionalValue", new TypeToken<Integer>(){}, SampleValueUsingCustomConcrete::optionalValue, Builder::optionalValue);
        public final PropertyMeta<SampleValueUsingCustomConcrete, Integer> value = PropertyMeta.<SampleValueUsingCustomConcrete, Integer, Builder>create(this, "value", new TypeToken<Integer>(){}, SampleValueUsingCustomConcrete::value, Builder::value);

        Meta() {
            propertyMap.put("optionalValue", optionalValue);
            propertyMap.put("value", value);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleValueUsingCustomConcrete> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleValueUsingCustomConcrete, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleValueUsingCustomConcrete, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleValueUsingCustomConcrete, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleValueUsingCustomConcrete, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleValueUsingCustomConcrete.builder();
        }

        @Override
        public int hashCode() {
            return Objects.hash(objectClass, builderClass);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Meta
                    && Objects.equals(((Meta)obj).objectClass(), objectClass())
                    && Objects.equals(((Meta)obj).builderClass(), builderClass());
        }
    }

    @JsonCreator public static SampleValueUsingCustomConcrete create(
            @JsonProperty("optionalValue") Integer optionalValue,
            @JsonProperty("value") int value) {
        return SampleValueUsingCustomConcrete.builder()
                .optionalValue(optionalValue)
                .value(value)
                .build();
    }

    public static SampleValueUsingCustomConcrete create(
            int value) {
        return SampleValueUsingCustomConcrete.builder()
                .value(value)
                .build();
    }

    @JsonIgnore public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValueUsingCustomConcrete, Builder>, SampleValueUsingCustomProtoBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleValueUsingCustomConcrete.Builder();
        }

        @Override
        Builder optionalValue(Integer optionalValue);

        @Override
        Builder value(int value);
    }

    @Override
    @Nullable
    @JsonProperty
    public abstract Integer optionalValue();

    @Override
    @JsonProperty
    public abstract int value();

}
