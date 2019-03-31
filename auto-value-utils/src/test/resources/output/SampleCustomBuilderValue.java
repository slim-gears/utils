package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
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
public abstract class SampleCustomBuilderValue implements SampleCustomBuilderValuePrototype, HasMetaClass<com.slimgears.sample.SampleCustomBuilderValue> {

    @Override
    public MetaClass<SampleCustomBuilderValue> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleCustomBuilderValue> {
        private final TypeToken<SampleCustomBuilderValue> objectClass = new TypeToken<SampleCustomBuilderValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleCustomBuilderValue, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleCustomBuilderValue, Double> doubleValue = PropertyMeta.<SampleCustomBuilderValue, Double, Builder>create(this, "doubleValue", new TypeToken<Double>(){}, SampleCustomBuilderValue::doubleValue, Builder::doubleValue);
        public final PropertyMeta<SampleCustomBuilderValue, Integer> intValue = PropertyMeta.<SampleCustomBuilderValue, Integer, Builder>create(this, "intValue", new TypeToken<Integer>(){}, SampleCustomBuilderValue::intValue, Builder::intValue);

        Meta() {
            propertyMap.put("doubleValue", doubleValue);
            propertyMap.put("intValue", intValue);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleCustomBuilderValue> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleCustomBuilderValue, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleCustomBuilderValue, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleCustomBuilderValue, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleCustomBuilderValue, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleCustomBuilderValue.builder();
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
    public static SampleCustomBuilderValue create(
            @JsonProperty("doubleValue") double doubleValue,
            @JsonProperty("intValue") int intValue) {
        return SampleCustomBuilderValue.builder()
                .doubleValue(doubleValue)
                .intValue(intValue)
                .build();
    }

    @Override
    public abstract double doubleValue();

    @Override
    public abstract int intValue();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleCustomBuilderValue, Builder>, SampleCustomBuilderValuePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleCustomBuilderValue.Builder();
        }

        @Override
        Builder doubleValue(double doubleValue);

        @Override
        Builder intValue(int intValue);
    }
}
