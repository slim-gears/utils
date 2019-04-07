package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClassWithKey;
import com.slimgears.util.autovalue.annotations.Key;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleWithKey implements SampleWithKeyPrototype, HasMetaClassWithKey<String, com.slimgears.sample.SampleWithKey> {

    @Override
    public MetaClassWithKey<String, SampleWithKey> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClassWithKey<String, SampleWithKey> {
        private final TypeToken<SampleWithKey> objectClass = new TypeToken<SampleWithKey>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleWithKey, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleWithKey, String> id = PropertyMeta.<SampleWithKey, String, Builder>create(this, "id", new TypeToken<String>(){}, SampleWithKey::id, Builder::id);
        public final PropertyMeta<SampleWithKey, String> text = PropertyMeta.<SampleWithKey, String, Builder>create(this, "text", new TypeToken<String>(){}, SampleWithKey::text, Builder::text);
        public final PropertyMeta<SampleWithKey, Integer> number = PropertyMeta.<SampleWithKey, Integer, Builder>create(this, "number", new TypeToken<Integer>(){}, SampleWithKey::number, Builder::number);

        @Override
        public PropertyMeta<SampleWithKey, String> keyProperty() {
            return id;
        }

        Meta() {
            propertyMap.put("id", id);
            propertyMap.put("text", text);
            propertyMap.put("number", number);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleWithKey> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleWithKey, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleWithKey, __V> getProperty(String name) {
            return (PropertyMeta<SampleWithKey, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleWithKey, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleWithKey.builder();
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

    public static SampleWithKey create(
         String id,
         String text,
         int number) {
        return SampleWithKey.builder()
            .id(id)
            .text(text)
            .number(number)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleWithKey, Builder>, SampleWithKeyPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleWithKey.Builder();
        }

        @Override
        @Key
        Builder id(String id);

        @Override
        Builder text(String text);

        @Override
        Builder number(int number);
    }

    @Override public abstract String id();
    @Override public abstract String text();
    @Override public abstract int number();

}
