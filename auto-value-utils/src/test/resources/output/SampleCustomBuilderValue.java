package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleCustomBuilderValue implements SampleCustomBuilderValuePrototype, HasMetaClass<SampleCustomBuilderValue, SampleCustomBuilderValue.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleCustomBuilderValue, SampleCustomBuilderValue.Builder> {

        private final Map<String, PropertyMeta<SampleCustomBuilderValue, Builder, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<SampleCustomBuilderValue, Builder, Integer> intValue = PropertyMeta.create("intValue", new TypeToken<Integer>(){}, SampleCustomBuilderValue::intValue, Builder::intValue);
        public final PropertyMeta<SampleCustomBuilderValue, Builder, Double> doubleValue = PropertyMeta.create("doubleValue", new TypeToken<Double>(){}, SampleCustomBuilderValue::doubleValue, Builder::doubleValue);

        Meta() {
            propertyMap.put("intValue", intValue);
            propertyMap.put("doubleValue", doubleValue);
        }

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
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleCustomBuilderValue, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleCustomBuilderValue, Builder, __V>)propertyMap.get(name);
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
        return metaClass;
    }

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleCustomBuilderValue create(
            @JsonProperty("intValue") int intValue,
            @JsonProperty("doubleValue") double doubleValue) {
        return SampleCustomBuilderValue.builder()
                .intValue(intValue)
                .doubleValue(doubleValue)
                .build();
    }

    @Override
    public abstract int intValue();

    @Override
    public abstract double doubleValue();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleCustomBuilderValue, Builder>, SampleCustomBuilderValuePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleCustomBuilderValue.Builder();
        }

        @Override
        Builder intValue(int intValue);

        @Override
        Builder doubleValue(double doubleValue);
    }
}
