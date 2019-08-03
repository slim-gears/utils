package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.google.common.reflect.TypeToken;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaBuilder;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericListContainer<T> implements GenericListContainerPrototype<T>, HasMetaClass<GenericListContainer<T>> {

    @Override
    
    public MetaClass<GenericListContainer<T>> metaClass() {
        return (MetaClass<GenericListContainer<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<GenericListContainer<T>> {
        private final TypeToken<GenericListContainer<T>> objectType = new TypeToken<GenericListContainer<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<GenericListContainer<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<GenericListContainer<T>, List<GenericListItem<T>>> items = PropertyMeta.<GenericListContainer<T>, List<GenericListItem<T>>, Builder<T>>create(this, "items", new TypeToken<List<GenericListItem<T>>>(){}, obj -> obj.items(), Builder::items);

        Meta() {
            propertyMap.put("items", items);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<GenericListContainer<T>> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<GenericListContainer<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<GenericListContainer<T>, __V> getProperty(String name) {
            return (PropertyMeta<GenericListContainer<T>, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<GenericListContainer<T>>> B createBuilder() {
            return (B)(BuilderPrototype)GenericListContainer.builder();
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

    public static <T> GenericListContainer<T> create(
         List<GenericListItem<T>> items) {
        return GenericListContainer.<T>builder()
            .items(items)
            .build();
    }

    public abstract Builder<T> toBuilder();

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<GenericListContainer<T>, Builder<T>>, GenericListContainerPrototypeBuilder<T, Builder<T>>{
        public static <T> Builder<T> create() {
            return new AutoValue_GenericListContainer.Builder<>();
        }

        @Override
        Builder<T> items(List<GenericListItem<T>> items);
    }

    @Override public abstract List<GenericListItem<T>> items();

}
