package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
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
public abstract class SampleSpecializedGeneric implements SampleSpecializedGenericPrototype, HasMetaClass<com.slimgears.sample.SampleSpecializedGeneric> {

    public MetaClass<SampleSpecializedGeneric> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleSpecializedGeneric> {
        private final TypeToken<SampleSpecializedGeneric> objectClass = new TypeToken<SampleSpecializedGeneric>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleSpecializedGeneric, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleSpecializedGeneric, ImmutableList<String>> values = PropertyMeta.<SampleSpecializedGeneric, ImmutableList<String>, Builder>create(this, "values", new TypeToken<ImmutableList<String>>(){}, SampleSpecializedGeneric::values, Builder::values);

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
        public Iterable<PropertyMeta<SampleSpecializedGeneric, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleSpecializedGeneric, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleSpecializedGeneric, __V>)propertyMap.get(name);
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
