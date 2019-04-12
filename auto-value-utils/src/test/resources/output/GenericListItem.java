package com.slimgears.sample;

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
public abstract class GenericListItem<T> implements GenericListItemPrototype<T>, HasMetaClass<GenericListItem<T>> {

    @Override
    
    public MetaClass<GenericListItem<T>> metaClass() {
        return (MetaClass<GenericListItem<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<GenericListItem<T>> {
        private final TypeToken<GenericListItem<T>> objectClass = new TypeToken<GenericListItem<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<GenericListItem<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<GenericListItem<T>, T> entry = PropertyMeta.<GenericListItem<T>, T, Builder<T>>create(this, "entry", new TypeToken<T>(){}, obj -> obj.entry(), Builder::entry);

        Meta() {
            propertyMap.put("entry", entry);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<GenericListItem<T>> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<GenericListItem<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<GenericListItem<T>, __V> getProperty(String name) {
            return (PropertyMeta<GenericListItem<T>, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<GenericListItem<T>, B>> B createBuilder() {
            return (B)(BuilderPrototype)GenericListItem.builder();
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

    public static <T> GenericListItem<T> create(
         T entry) {
        return GenericListItem.<T>builder()
            .entry(entry)
            .build();
    }

    public abstract Builder<T> toBuilder();

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<GenericListItem<T>, Builder<T>>, GenericListItemPrototypeBuilder<T, Builder<T>> {
        public static <T> Builder<T> create() {
            return new AutoValue_GenericListItem.Builder<>();
        }

        @Override
        Builder<T> entry(T entry);
    }

    @Override public abstract T entry();

}
