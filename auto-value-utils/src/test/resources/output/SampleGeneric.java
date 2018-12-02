package com.slimgears.sample;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleGeneric.Builder.class)
@JsonSerialize(as = SampleGeneric.class)
public abstract class SampleGeneric<T> implements SampleGenericPrototype<T>, HasMetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> {
    public static final Meta metaClass = new Meta();

    public static class Meta<T> implements MetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> {

        private final Map<String, PropertyMeta<SampleGeneric<T>, Builder<T>, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<SampleGeneric<T>, Builder<T>, String> value = PropertyMeta.create("value", new TypeToken<String>(){}, SampleGeneric::value, Builder::value);
        public final PropertyMeta<SampleGeneric<T>, Builder<T>, T> tValue = PropertyMeta.create("tValue", new TypeToken<T>(){}, SampleGeneric::tValue, Builder::tValue);

        Meta() {
            propertyMap.put("value", value);
            propertyMap.put("tValue", tValue);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return new TypeToken<Builder<T>>(){};
        }

        @Override
        public TypeToken<SampleGeneric<T>> objectClass() {
            return new TypeToken<SampleGeneric<T>>(){};
        }

        @Override
        public Iterable<PropertyMeta<SampleGeneric<T>, Builder<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleGeneric<T>, Builder<T>, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleGeneric<T>, Builder<T>, __V>)propertyMap.get(name);
        }

        @Override
        public Builder<T> createBuilder() {
            return SampleGeneric.builder();
        }
    }

    @JsonIgnore
    public abstract Builder<T> toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> metaClass() {
        return new Meta<>();
    }

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @Override
    public abstract String value();

    @Override
    public abstract T tValue();

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<SampleGeneric<T>, Builder<T>>, SampleGenericPrototypeBuilder<T, Builder<T>> {
        @JsonCreator
        public static <T> Builder<T> create() {
            return new AutoValue_SampleGeneric.Builder<>();
        }

        @Override
        Builder<T> value(String value);

        @Override
        Builder<T> tValue(T tValue);
    }
}
