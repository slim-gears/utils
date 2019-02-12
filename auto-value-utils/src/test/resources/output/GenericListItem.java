package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ObjectExpression;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.expressions.PropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.ObjectPropertyExpression;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericListItem<T> implements GenericListItemPrototype<T>, HasMetaClass<GenericListItem<T>, GenericListItem.Builder<T>> {
    public static final Expressions<GenericListItem, ?> $ = new Expressions<>();
    public static final Meta metaClass = new Meta();

    public static <T> Expressions<GenericListItem, T> $() {
        return new Expressions<>();
    }

    public static class Expressions<S, T> {
        private final ObjectExpression<S, GenericListItem<T>> self = ObjectExpression.arg();
        private final Meta<T> meta = new Meta<>();
        public final ObjectPropertyExpression<S, GenericListItem<T>, Builder<T>, T> entry = PropertyExpression.ofObject(self, meta.entry);
    }

    public static class ReferencePropertyExpression<__S, __T, __B, T> extends Expressions<__S, T> implements PropertyExpression<__S, __T, __B, GenericListItem<T>> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, GenericListItem<T>> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, GenericListItem<T>> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B, T> ReferencePropertyExpression<__S, __T, __B, T> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, GenericListItem<T>> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, GenericListItem<T>> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

    public static class Meta<T> implements MetaClass<GenericListItem<T>, GenericListItem.Builder<T>> {
        private final TypeToken<GenericListItem<T>> objectClass = new TypeToken<GenericListItem<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<GenericListItem<T>, Builder<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<GenericListItem<T>, Builder<T>, T> entry = PropertyMeta.create(objectClass, "entry", new TypeToken<T>(){}, GenericListItem::entry, Builder::entry);

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
