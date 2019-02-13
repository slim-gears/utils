package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.CollectionPropertyExpression;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleSpecializedGeneric implements SampleSpecializedGenericPrototype, HasMetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> {
    public static final Meta metaClass = new Meta();

    public static final Expressions<SampleSpecializedGeneric> $ = new Expressions<>();
    public static Expressions<SampleSpecializedGeneric> $() {
        return $;
    }

    public static class Expressions<__S> {
        private final ObjectExpression<__S, SampleSpecializedGeneric> self = ObjectExpression.arg();
        private final Meta meta = new Meta() ;

        public final CollectionPropertyExpression<__S, SampleSpecializedGeneric, Builder, String> values = PropertyExpression.ofCollection(self, meta.values);
    }

    public static class ReferencePropertyExpression<__S, __T, __B> extends Expressions<__S> implements PropertyExpression<__S, __T, __B, SampleSpecializedGeneric> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, SampleSpecializedGeneric> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleSpecializedGeneric> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B> ReferencePropertyExpression<__S, __T, __B> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleSpecializedGeneric> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, SampleSpecializedGeneric> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

    public static class Meta implements MetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> {
        private final TypeToken<SampleSpecializedGeneric> objectClass = new TypeToken<SampleSpecializedGeneric>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleSpecializedGeneric, Builder, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleSpecializedGeneric, Builder, ImmutableList<String>> values = PropertyMeta.create(objectClass, "values", new TypeToken<ImmutableList<String>>(){}, SampleSpecializedGeneric::values, Builder::values);

        Meta() {
            propertyMap.put("values", values);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleSpecializedGeneric> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleSpecializedGeneric, Builder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleSpecializedGeneric, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleSpecializedGeneric, Builder, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleSpecializedGeneric.builder();
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
    public abstract Builder toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> metaClass() {
        return metaClass;
    }

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleSpecializedGeneric create(
            @JsonProperty("values") ImmutableList<String> values) {
        return SampleSpecializedGeneric.builder()
                .values(values)
                .build();
    }

    @Override
    public abstract ImmutableList<String> values();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleSpecializedGeneric, Builder>, SampleSpecializedGenericPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleSpecializedGeneric.Builder();
        }

        @Override
        Builder values(ImmutableList<String> values);
        ImmutableList.Builder<String> valuesBuilder();

    }
}
