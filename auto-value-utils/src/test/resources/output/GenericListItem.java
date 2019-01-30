package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
public abstract class GenericListItem<T> implements GenericListItemPrototype<T>, HasMetaClass<GenericListItem<T>, GenericListItem.Builder<T>> {
    public static final Meta metaClass = new Meta();

    public static class Meta<T> implements MetaClass<GenericListItem<T>, GenericListItem.Builder<T>> {

        private final Map<String, PropertyMeta<GenericListItem<T>, Builder<T>, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<GenericListItem<T>, Builder<T>, T> entry = PropertyMeta.create("entry", new TypeToken<T>(){}, GenericListItem::entry, Builder::entry);

        Meta() {
            propertyMap.put("entry", entry);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return new TypeToken<Builder<T>>(){};
        }

        @Override
        public TypeToken<GenericListItem<T>> objectClass() {
            return new TypeToken<GenericListItem<T>>(){};
        }

        @Override
        public Iterable<PropertyMeta<GenericListItem<T>, Builder<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<GenericListItem<T>, Builder<T>, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<GenericListItem<T>, Builder<T>, __V>)propertyMap.get(name);
        }

        @Override
        public Builder<T> createBuilder() {
            return GenericListItem.builder();
        }
    }

    @JsonIgnore
    public abstract Builder<T> toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<GenericListItem<T>, GenericListItem.Builder<T>> metaClass() {
        return new Meta<>();
    }

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @JsonCreator
    public static <T> GenericListItem<T> create(
            @JsonProperty("entry") T entry) {
        return GenericListItem.<T>builder()
                .entry(entry)
                .build();
    }

    @Override
    public abstract T entry();

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<GenericListItem<T>, Builder<T>>, GenericListItemPrototypeBuilder<T, Builder<T>> {
        public static <T> Builder<T> create() {
            return new AutoValue_GenericListItem.Builder<>();
        }

        @Override
        Builder<T> entry(T entry);
    }
}
