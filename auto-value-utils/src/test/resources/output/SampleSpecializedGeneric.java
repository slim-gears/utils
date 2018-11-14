package com.slimgears.sample;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.reflect.TypeToken;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleSpecializedGeneric.Builder.class)
@JsonSerialize(as = SampleSpecializedGeneric.class)
public abstract class SampleSpecializedGeneric implements SampleSpecializedGenericPrototype, HasMetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder> {
        public final PropertyMeta<SampleSpecializedGeneric, Builder, ImmutableList<String>> values = PropertyMeta.create("values", new TypeToken<ImmutableList<String>>(){}, SampleSpecializedGeneric::values, Builder::values);
        public final ImmutableList<PropertyMeta<SampleSpecializedGeneric, Builder, ?>> allProperties = ImmutableList.<PropertyMeta<SampleSpecializedGeneric, Builder, ?>>builder()
                .add(values)
                .build();

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
            return allProperties;
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
        //noinspection unchecked
        return (MetaClass<SampleSpecializedGeneric, SampleSpecializedGeneric.Builder>)metaClass;
    }

    public static Builder builder() {
        return Builder.create();
    }

    @Override
    public abstract ImmutableList<String> values();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleSpecializedGeneric, Builder>, SampleSpecializedGenericPrototypeBuilder<Builder> {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_SampleSpecializedGeneric.Builder();
        }

        @Override
        Builder values(ImmutableList<String> values);
        ImmutableList.Builder<String> valuesBuilder();

    }
}
