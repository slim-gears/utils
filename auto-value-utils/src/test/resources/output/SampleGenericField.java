package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.reflect.TypeToken;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleGenericField implements SampleGenericFieldPrototype, HasMetaClass<com.slimgears.sample.SampleGenericField> {

    @Override
    public MetaClass<SampleGenericField> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleGenericField> {
        private final TypeToken<SampleGenericField> objectClass = new TypeToken<SampleGenericField>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleGenericField, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleGenericField, Class<? extends Collection>> value = PropertyMeta.<SampleGenericField, Class<? extends Collection>, Builder>create(this, "value", new TypeToken<Class<? extends Collection>>(){}, SampleGenericField::value, Builder::value);

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
        public Iterable<PropertyMeta<SampleGenericField, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleGenericField, __V> getProperty(String name) {
            return (PropertyMeta<SampleGenericField, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleGenericField, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleGenericField.builder();
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

    public static SampleGenericField create(
         Class<? extends Collection> value) {
        return SampleGenericField.builder()
            .value(value)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleGenericField, Builder>, SampleGenericFieldPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleGenericField.Builder();
        }

        @Override
        Builder value(Class<? extends Collection> value);
    }

    @Override public abstract Class<? extends Collection> value();
}
