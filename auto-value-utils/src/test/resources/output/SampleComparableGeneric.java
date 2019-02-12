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
public abstract class SampleComparableGeneric<T extends Comparable<T>> implements SampleComparableGenericPrototype<T>, HasMetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>> {
    public static final Expressions<SampleComparableGeneric, ?> $ = new Expressions<>();
    public static final Meta metaClass = new Meta();

    public static <T extends Comparable<T>> Expressions<SampleComparableGeneric, T> $() {
        return new Expressions<>();
    }

    public static class Expressions<S, T extends java.lang.Comparable<T>> {
        private final ObjectExpression<S, SampleComparableGeneric<T>> self = ObjectExpression.arg();
        private final Meta<T> meta = new Meta<>();
        public final ObjectPropertyExpression<S, SampleComparableGeneric<T>, Builder<T>, T> tValue = PropertyExpression.ofObject(self, meta.tValue);
    }

    public static class ReferencePropertyExpression<__S, __T, __B, T extends java.lang.Comparable<T>> extends Expressions<__S, T> implements PropertyExpression<__S, __T, __B, SampleComparableGeneric<T>> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, SampleComparableGeneric<T>> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleComparableGeneric<T>> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B, T extends java.lang.Comparable<T>> ReferencePropertyExpression<__S, __T, __B, T> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleComparableGeneric<T>> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, SampleComparableGeneric<T>> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

    public static class Meta<T extends Comparable<T>> implements MetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>> {
        private final TypeToken<SampleComparableGeneric<T>> objectClass = new TypeToken<SampleComparableGeneric<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<SampleComparableGeneric<T>, Builder<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleComparableGeneric<T>, Builder<T>, T> tValue = PropertyMeta.create(objectClass, "tValue", new TypeToken<T>(){}, SampleComparableGeneric::tValue, Builder::tValue);

        Meta() {
            propertyMap.put("tValue", tValue);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleComparableGeneric<T>> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleComparableGeneric<T>, Builder<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleComparableGeneric<T>, Builder<T>, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleComparableGeneric<T>, Builder<T>, __V>)propertyMap.get(name);
        }

        @Override
        public Builder<T> createBuilder() {
            return SampleComparableGeneric.builder();
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
    public MetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>> metaClass() {
        return new Meta<>();
    }

    public static <T extends Comparable<T>> Builder<T> builder() {
        return Builder.create();
    }

    @JsonCreator
    public static <T extends Comparable<T>> SampleComparableGeneric<T> create(
            @JsonProperty("tValue") T tValue) {
        return SampleComparableGeneric.<T>builder()
                .tValue(tValue)
                .build();
    }

    @Override
    public abstract T tValue();

    @AutoValue.Builder
    public interface Builder<T extends Comparable<T>> extends BuilderPrototype<SampleComparableGeneric<T>, Builder<T>>, SampleComparableGenericPrototypeBuilder<T, Builder<T>> {
        public static <T extends Comparable<T>> Builder<T> create() {
            return new AutoValue_SampleComparableGeneric.Builder<>();
        }

        @Override
        Builder<T> tValue(T tValue);
    }
}
