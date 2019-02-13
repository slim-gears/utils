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
import com.slimgears.util.autovalue.expressions.internal.StringPropertyExpression;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleGeneric<T> implements SampleGenericPrototype<T>, HasMetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> {
    public static final Expressions<SampleGeneric, ?> $ = new Expressions<>();
    public static final Meta metaClass = new Meta();

    public static <T> Expressions<SampleGeneric, T> $() {
        return new Expressions<>();
    }

    public static class Expressions<__S, T> {
        private final ObjectExpression<__S, SampleGeneric<T>> self = ObjectExpression.arg();
        private final Meta<T> meta = new Meta<>();
        public final StringPropertyExpression<__S, SampleGeneric<T>, Builder<T>> value = PropertyExpression.ofString(self, meta.value);
        public final ObjectPropertyExpression<__S, SampleGeneric<T>, Builder<T>, T> tValue = PropertyExpression.ofObject(self, meta.tValue);
    }

    public static class ReferencePropertyExpression<__S, __T, __B, T> extends Expressions<__S, T> implements PropertyExpression<__S, __T, __B, SampleGeneric<T>> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, SampleGeneric<T>> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleGeneric<T>> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B, T> ReferencePropertyExpression<__S, __T, __B, T> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleGeneric<T>> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, SampleGeneric<T>> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

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

    @JsonIgnore
    @Override
    public MetaClass<SampleGeneric<T>, SampleGeneric.Builder<T>> metaClass() {
        return new Meta<>();
    }

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
