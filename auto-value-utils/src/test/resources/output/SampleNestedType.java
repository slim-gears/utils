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
public abstract class SampleNestedType implements SampleNestedTypePrototype, HasMetaClass<com.slimgears.sample.SampleNestedType> {

    @Override
    public MetaClass<SampleNestedType> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleNestedType> {
        private final TypeToken<SampleNestedType> objectClass = new TypeToken<SampleNestedType>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleNestedType, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleNestedType, SampleNestedTypePrototype.NestedEnum> value = PropertyMeta.<SampleNestedType, SampleNestedTypePrototype.NestedEnum, Builder>create(this, "value", new TypeToken<SampleNestedTypePrototype.NestedEnum>(){}, SampleNestedType::value, Builder::value);

        Meta() {
            propertyMap.put("value", value);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleNestedType> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleNestedType, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleNestedType, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleNestedType, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleNestedType.builder();
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
    public static SampleNestedType create(
            @JsonProperty("value") SampleNestedTypePrototype.NestedEnum value) {
        return SampleNestedType.builder()
                .value(value)
                .build();
    }

    @Override
    public abstract SampleNestedTypePrototype.NestedEnum value();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleNestedType, Builder>, SampleNestedTypePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleNestedType.Builder();
        }

        @Override
        Builder value(SampleNestedTypePrototype.NestedEnum value);
    }
}
