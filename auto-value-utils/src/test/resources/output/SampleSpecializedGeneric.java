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
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleSpecializedGeneric implements SampleSpecializedGenericPrototype, HasMetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> {

        private final Map<String, PropertyMeta<SampleSpecializedGeneric, Builder, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<SampleSpecializedGeneric, Builder, ImmutableList<String>> values = PropertyMeta.create("values", new TypeToken<ImmutableList<String>>(){}, SampleSpecializedGeneric::values, Builder::values);

        Meta() {
            propertyMap.put("values", values);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return new TypeToken<Builder>(){};
        }

        @Override
        public TypeToken<SampleSpecializedGeneric> objectClass() {
            return new TypeToken<SampleSpecializedGeneric>(){};
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
