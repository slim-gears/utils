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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class GenericListContainer<T> implements GenericListContainerPrototype<T>, HasMetaClass<GenericListContainer<T>, GenericListContainer.Builder<T>> {
    public static final Expressions<GenericListContainer, ?> $ = new Expressions<>();
    public static final Meta metaClass = new Meta();

    public static <T> Expressions<GenericListContainer, T> $() {
        return new Expressions<>();
    }

    public static class Expressions<S, T> {
        private final ObjectExpression<S, GenericListContainer<T>> self = ObjectExpression.arg();
        private final Meta<T> meta = new Meta<>();
        public final ObjectPropertyExpression<S, GenericListContainer<T>, Builder<T>, List<GenericListItem<T>>> items = PropertyExpression.ofObject(self, meta.items);
    }

    public static class ReferencePropertyExpression<__S, __T, __B, T> extends Expressions<__S, T> implements PropertyExpression<__S, __T, __B, GenericListContainer<T>> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, GenericListContainer<T>> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, GenericListContainer<T>> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B, T> ReferencePropertyExpression<__S, __T, __B, T> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, GenericListContainer<T>> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, GenericListContainer<T>> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

    public static class Meta<T> implements MetaClass<GenericListContainer<T>, GenericListContainer.Builder<T>> {
        private final TypeToken<GenericListContainer<T>> objectClass = new TypeToken<GenericListContainer<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<GenericListContainer<T>, Builder<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<GenericListContainer<T>, Builder<T>, List<GenericListItem<T>>> items = PropertyMeta.create(objectClass, "items", new TypeToken<List<GenericListItem<T>>>(){}, GenericListContainer::items, Builder::items);

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
        public Iterable<PropertyMeta<GenericListContainer<T>, Builder<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<GenericListContainer<T>, Builder<T>, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<GenericListContainer<T>, Builder<T>, __V>)propertyMap.get(name);
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

    @JsonIgnore
    @Override
    public MetaClass<GenericListContainer<T>, GenericListContainer.Builder<T>> metaClass() {
        return new Meta<>();
    }

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
