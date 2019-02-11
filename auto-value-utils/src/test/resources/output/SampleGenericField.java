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
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleGenericField implements SampleGenericFieldPrototype, HasMetaClass<SampleGenericField, SampleGenericField.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleGenericField, SampleGenericField.Builder> {

        private final TypeToken<SampleGenericField> objectClass = new TypeToken<SampleGenericField>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleGenericField, Builder, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<SampleGenericField, Builder, Class<? extends Collection>> value = PropertyMeta.create(objectClass, "value", new TypeToken<Class<? extends Collection>>(){}, SampleGenericField::value, Builder::value);

        Meta() {
            propertyMap.put("value", value);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleGenericField> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleGenericField, Builder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleGenericField, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleGenericField, Builder, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleGenericField.builder();
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
    public MetaClass<SampleGenericField, SampleGenericField.Builder> metaClass() {
        return metaClass;
    }

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleGenericField create(
            @JsonProperty("value") Class<? extends Collection> value) {
        return SampleGenericField.builder()
                .value(value)
                .build();
    }

    @Override
    public abstract Class<? extends Collection> value();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleGenericField, Builder>, SampleGenericFieldPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleGenericField.Builder();
        }

        @Override
        Builder value(Class<? extends Collection> value);
    }
}
