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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericListContainer<T> implements GenericListContainerPrototype<T>, HasMetaClass<com.slimgears.sample.GenericListContainer<T>> {

    public MetaClass<GenericListContainer<T>> metaClass() {
        return (MetaClass<GenericListContainer<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<GenericListContainer<T>> {
        private final TypeToken<GenericListContainer<T>> objectClass = new TypeToken<GenericListContainer<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<GenericListContainer<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<GenericListContainer<T>, List<GenericListItem<T>>> items = PropertyMeta.<GenericListContainer<T>, List<GenericListItem<T>>, Builder<T>>create(this, "items", new TypeToken<List<GenericListItem<T>>>(){}, GenericListContainer::items, Builder::items);

        Meta() {
            propertyMap.put("items", items);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<GenericListContainer<T>> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<GenericListContainer<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<GenericListContainer<T>, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<GenericListContainer<T>, __V>)propertyMap.get(name);
        }

        @Override
        public Builder<T> createBuilder() {
            return GenericListContainer.builder();
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
    public static <T> GenericListContainer<T> create(
            @JsonProperty("items") List<GenericListItem<T>> items) {
        return GenericListContainer.<T>builder()
                .items(items)
                .build();
    }

    @Override
    public abstract List<GenericListItem<T>> items();

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<GenericListContainer<T>, Builder<T>>, GenericListContainerPrototypeBuilder<T, Builder<T>> {
        public static <T> Builder<T> create() {
            return new AutoValue_GenericListContainer.Builder<>();
        }

        @Override
        Builder<T> items(List<GenericListItem<T>> items);
    }
}
