package com.slimgears.sample;

import com.google.common.collect.ImmutableList;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.reflect.TypeToken;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
@JsonDeserialize(builder = SampleComparableGeneric.Builder.class)
@JsonSerialize(as = SampleComparableGeneric.class)
public abstract class SampleComparableGeneric<T extends Comparable<T>> implements SampleComparableGenericPrototype<T>, HasMetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>> {
    public static final Meta metaClass = new Meta();

    public static class Meta<T extends Comparable<T>> implements MetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>> {
        public final PropertyMeta<SampleComparableGeneric<T>, Builder<T>, T> tValue = PropertyMeta.create("tValue", new TypeToken<T>(){}, SampleComparableGeneric::tValue, Builder::tValue);
        public final ImmutableList<PropertyMeta<SampleComparableGeneric<T>, Builder<T>, ?>> allProperties = ImmutableList.<PropertyMeta<SampleComparableGeneric<T>, Builder<T>, ?>>builder()
                .add(tValue)
                .build();

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return new TypeToken<Builder<T>>(){};
        }

        @Override
        public TypeToken<SampleComparableGeneric<T>> objectClass() {
            return new TypeToken<SampleComparableGeneric<T>>(){};
        }

        @Override
        public Iterable<PropertyMeta<SampleComparableGeneric<T>, Builder<T>, ?>> properties() {
            return allProperties;
        }

        @Override
        public Builder<T> createBuilder() {
            return SampleComparableGeneric.builder();
        }
    }

    @JsonIgnore
    public abstract Builder<T> toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>> metaClass() {
        //noinspection unchecked
        return (MetaClass<SampleComparableGeneric<T>, SampleComparableGeneric.Builder<T>>)metaClass;
    }

    public static <T extends Comparable<T>> Builder<T> builder() {
        return Builder.create();
    }

    @Override
    public abstract T tValue();

    @AutoValue.Builder
    public interface Builder<T extends Comparable<T>> extends BuilderPrototype<SampleComparableGeneric<T>, Builder<T>>, SampleComparableGenericPrototypeBuilder<T, Builder<T>> {
        @JsonCreator
        public static <T extends Comparable<T>> Builder<T> create() {
            return new AutoValue_SampleComparableGeneric.Builder<>();
        }

        @Override
        Builder<T> tValue(T tValue);
    }
}
