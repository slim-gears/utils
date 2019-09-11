package com.slimgears.util.sample;

import com.google.auto.value.AutoValue;
import com.google.common.reflect.TypeToken;
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
public abstract class StringIntegerInterface implements StringIntegerInterfacePrototype, HasMetaClass<StringIntegerInterface> {

    @Override
    
    public MetaClass<StringIntegerInterface> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<StringIntegerInterface> {
        private final TypeToken<StringIntegerInterface> objectType = new TypeToken<StringIntegerInterface>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<StringIntegerInterface, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<StringIntegerInterface, String> name = PropertyMeta.<StringIntegerInterface, String, Builder>create(this, "name", new PropertyType<String>(){}, obj -> obj.name(), Builder::name);
        public final PropertyMeta<StringIntegerInterface, String> key = PropertyMeta.<StringIntegerInterface, String, Builder>create(this, "key", new PropertyType<String>(){}, obj -> obj.key(), Builder::key);
        public final PropertyMeta<StringIntegerInterface, Integer> value = PropertyMeta.<StringIntegerInterface, Integer, Builder>create(this, "value", new PropertyType<Integer>(){}, obj -> obj.value(), Builder::value);

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
        public TypeToken<StringIntegerInterface> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<StringIntegerInterface, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<StringIntegerInterface, __V> getProperty(String name) {
            return (PropertyMeta<StringIntegerInterface, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<StringIntegerInterface>> B createBuilder() {
            return (B)(BuilderPrototype)StringIntegerInterface.builder();
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

    public static StringIntegerInterface create(
         String name,
         String key,
         Integer value) {
        return StringIntegerInterface.builder()
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
    public interface Builder extends BuilderPrototype<StringIntegerInterface, Builder>, StringIntegerInterfacePrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_StringIntegerInterface.Builder();
        }

        @Override
        Builder name(String name);

        @Override
        Builder key(String key);

        @Override
        Builder value(Integer value);
    }

    @Override public abstract String name();
    @Override public abstract String key();
    @Override public abstract Integer value();

}
