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
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleValue implements SampleValuePrototype, HasMetaClass<SampleValue, SampleValue.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleValue, SampleValue.Builder> {

        private final Map<String, PropertyMeta<SampleValue, Builder, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<SampleValue, Builder, Integer> intValue = PropertyMeta.create("intValue", new TypeToken<Integer>(){}, SampleValue::intValue, Builder::intValue);
        public final PropertyMeta<SampleValue, Builder, Double> doubleValue = PropertyMeta.create("doubleValue", new TypeToken<Double>(){}, SampleValue::doubleValue, Builder::doubleValue);
        public final PropertyMeta<SampleValue, Builder, String> strValue = PropertyMeta.create("strValue", new TypeToken<String>(){}, SampleValue::strValue, Builder::strValue);
        public final PropertyMeta<SampleValue, Builder, Boolean> foo = PropertyMeta.create("foo", new TypeToken<Boolean>(){}, SampleValue::foo, Builder::foo);

        Meta() {
            propertyMap.put("intValue", intValue);
            propertyMap.put("doubleValue", doubleValue);
            propertyMap.put("strValue", strValue);
            propertyMap.put("foo", foo);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return new TypeToken<Builder>(){};
        }

        @Override
        public TypeToken<SampleValue> objectClass() {
            return new TypeToken<SampleValue>(){};
        }

        @Override
        public Iterable<PropertyMeta<SampleValue, Builder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleValue, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleValue, Builder, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleValue.builder();
        }
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<SampleValue, SampleValue.Builder> metaClass() {
        return metaClass;
    }

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleValue create(
            @JsonProperty("intValue") int intValue,
            @JsonProperty("doubleValue") double doubleValue,
            @JsonProperty("strValue") String strValue,
            @JsonProperty("foo") boolean foo) {
        return SampleValue.builder()
                .intValue(intValue)
                .doubleValue(doubleValue)
                .strValue(strValue)
                .foo(foo)
                .build();
    }

    @Override
    public abstract int intValue();

    @Override
    @SampleFieldAnnotation(strValue = "test")
    public abstract double doubleValue();

    @Override
    @SampleFieldAnnotation
    @Nullable
    public abstract String strValue();

    @Override
    public abstract boolean foo();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValue, Builder>, SampleValuePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleValue.Builder();
        }

        @Override
        Builder intValue(int intValue);

        @Override
        @SampleFieldAnnotation(strValue = "test")
        Builder doubleValue(double doubleValue);

        @Override
        @SampleFieldAnnotation
        Builder strValue(String strValue);

        @Override
        Builder foo(boolean foo);
    }
}
