package com.slimgears.util.sample;

import com.google.auto.value.AutoValue;
import com.google.common.reflect.TypeToken;
import com.slimgears.sample.SampleValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaBuilder;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.annotations.PropertyType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class StringSampleValueInterface implements StringSampleValueInterfacePrototype, HasMetaClass<StringSampleValueInterface> {

    @Override
    
    public MetaClass<StringSampleValueInterface> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<StringSampleValueInterface> {
        private final TypeToken<StringSampleValueInterface> objectType = new TypeToken<StringSampleValueInterface>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<StringSampleValueInterface, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<StringSampleValueInterface, String> name = PropertyMeta.<StringSampleValueInterface, String, Builder>create(this, "name", new PropertyType<String>(){}, obj -> obj.name(), Builder::name);
        public final PropertyMeta<StringSampleValueInterface, String> key = PropertyMeta.<StringSampleValueInterface, String, Builder>create(this, "key", new PropertyType<String>(){}, obj -> obj.key(), Builder::key);
        public final PropertyMeta<StringSampleValueInterface, SampleValue> value = PropertyMeta.<StringSampleValueInterface, SampleValue, Builder>create(this, "value", new PropertyType<SampleValue>(){}, obj -> obj.value(), Builder::value);

        Meta() {
            propertyMap.put("name", name);
            propertyMap.put("key", key);
            propertyMap.put("value", value);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<StringSampleValueInterface> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<StringSampleValueInterface, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<StringSampleValueInterface, __V> getProperty(String name) {
            return (PropertyMeta<StringSampleValueInterface, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<StringSampleValueInterface>> B createBuilder() {
            return (B)(BuilderPrototype)StringSampleValueInterface.builder();
        }

        @Override
        public int hashCode() {
            return Objects.hash(objectType, builderClass);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Meta
            && Objects.equals(((Meta)obj).asType(), asType())
            && Objects.equals(((Meta)obj).builderClass(), builderClass());
        }
    }

    public static StringSampleValueInterface create(
         String name,
         String key,
         SampleValue value) {
        return StringSampleValueInterface.builder()
            .name(name)
            .key(key)
            .value(value)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<StringSampleValueInterface, Builder>, StringSampleValueInterfacePrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_StringSampleValueInterface.Builder();
        }

        @Override
        Builder name(String name);

        @Override
        Builder key(String key);

        @Override
        Builder value(SampleValue value);
    }

    @Override public abstract String name();
    @Override public abstract String key();
    @Override public abstract SampleValue value();

}
