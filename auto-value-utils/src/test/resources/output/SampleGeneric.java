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

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleGeneric<T> implements SampleGenericPrototype<T>, HasMetaClass<com.slimgears.sample.SampleGeneric<T>, SampleGeneric.Builder<T>> {

    public MetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> metaClass() {
        return (MetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> {
        private final TypeToken<SampleGeneric<T>> objectClass = new TypeToken<SampleGeneric<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<SampleGeneric<T>, Builder<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleGeneric<T>, Builder<T>, String> value = PropertyMeta.create(objectClass, "value", new TypeToken<String>(){}, SampleGeneric::value, Builder::value);
        public final PropertyMeta<SampleGeneric<T>, Builder<T>, T> tValue = PropertyMeta.create(objectClass, "tValue", new TypeToken<T>(){}, SampleGeneric::tValue, Builder::tValue);

        Meta() {
            propertyMap.put("value", value);
            propertyMap.put("tValue", tValue);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleGeneric<T>> objectClass() {
            return this.objectClass;
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

    @JsonIgnore
    public abstract Builder<T> toBuilder();

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @JsonCreator
    public static <T> SampleGeneric<T> create(
            @JsonProperty("value") String value,
            @JsonProperty("tValue") T tValue) {
        return SampleGeneric.<T>builder()
                .value(value)
                .tValue(tValue)
                .build();
    }

    @Override
    public abstract String value();

    @Override
    public abstract T tValue();

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<SampleGeneric<T>, Builder<T>>, SampleGenericPrototypeBuilder<T, Builder<T>> {
        public static <T> Builder<T> create() {
            return new AutoValue_SampleGeneric.Builder<>();
        }

        @Override
        Builder<T> value(String value);

        @Override
        Builder<T> tValue(T tValue);
    }
}
