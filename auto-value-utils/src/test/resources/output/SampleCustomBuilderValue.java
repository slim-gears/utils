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
@JsonDeserialize(builder = SampleCustomBuilderValue.Builder.class)
@JsonSerialize(as = SampleCustomBuilderValue.class)
public abstract class SampleCustomBuilderValue implements SampleCustomBuilderValuePrototype, HasMetaClass<SampleCustomBuilderValue, SampleCustomBuilderValue.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleCustomBuilderValue, SampleCustomBuilderValue.Builder> {
        public final PropertyMeta<SampleCustomBuilderValue, Builder, Integer> intValue = PropertyMeta.create("intValue", new TypeToken<Integer>(){}, SampleCustomBuilderValue::intValue, Builder::intValue);
        public final PropertyMeta<SampleCustomBuilderValue, Builder, Double> doubleValue = PropertyMeta.create("doubleValue", new TypeToken<Double>(){}, SampleCustomBuilderValue::doubleValue, Builder::doubleValue);
        public final ImmutableList<PropertyMeta<SampleCustomBuilderValue, Builder, ?>> allProperties = ImmutableList.<PropertyMeta<SampleCustomBuilderValue, Builder, ?>>builder()
                .add(intValue)
                .add(doubleValue)
                .build();

        @Override
        public TypeToken<Builder> builderClass() {
            return new TypeToken<Builder>(){};
        }

        @Override
        public TypeToken<SampleCustomBuilderValue> objectClass() {
            return new TypeToken<SampleCustomBuilderValue>(){};
        }

        @Override
        public Iterable<PropertyMeta<SampleCustomBuilderValue, Builder, ?>> properties() {
            return allProperties;
        }

        @Override
        public Builder createBuilder() {
            return SampleCustomBuilderValue.builder();
        }
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<SampleCustomBuilderValue, SampleCustomBuilderValue.Builder> metaClass() {
        //noinspection unchecked
        return (MetaClass<SampleCustomBuilderValue, SampleCustomBuilderValue.Builder>)metaClass;
    }

    public static Builder builder() {
        return Builder.create();
    }

    @Override
    public abstract int intValue();

    @Override
    public abstract double doubleValue();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleCustomBuilderValue, Builder>, SampleCustomBuilderValuePrototypeBuilder<Builder> {
        @JsonCreator
        public static Builder create() {
            return new AutoValue_SampleCustomBuilderValue.Builder();
        }

        @Override
        Builder intValue(int intValue);

        @Override
        Builder doubleValue(double doubleValue);
    }
}
