package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.PropertyMeta;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasKeyProperty;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.Key;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleWithKey implements SampleWithKeyPrototype, HasMetaClass<SampleWithKey, SampleWithKey.Builder> {
    public static final Meta metaClass = new Meta();

    public static class Meta implements MetaClass<SampleWithKey, SampleWithKey.Builder>, HasKeyProperty<String, SampleWithKey, Builder> {

        private final Map<String, PropertyMeta<SampleWithKey, Builder, ?>> propertyMap = new LinkedHashMap<>();
        public final PropertyMeta<SampleWithKey, Builder, String> id = PropertyMeta.create("id", new TypeToken<String>(){}, SampleWithKey::id, Builder::id);
        public final PropertyMeta<SampleWithKey, Builder, String> text = PropertyMeta.create("text", new TypeToken<String>(){}, SampleWithKey::text, Builder::text);
        public final PropertyMeta<SampleWithKey, Builder, Integer> number = PropertyMeta.create("number", new TypeToken<Integer>(){}, SampleWithKey::number, Builder::number);

        @Override
        public PropertyMeta<SampleWithKey, Builder, String> keyProperty() {
            return id;
        }

        Meta() {
            propertyMap.put("id", id);
            propertyMap.put("text", text);
            propertyMap.put("number", number);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return new TypeToken<Builder>(){};
        }

        @Override
        public TypeToken<SampleWithKey> objectClass() {
            return new TypeToken<SampleWithKey>(){};
        }

        @Override
        public Iterable<PropertyMeta<SampleWithKey, Builder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleWithKey, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleWithKey, Builder, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleWithKey.builder();
        }
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    @JsonIgnore
    @Override
    public MetaClass<SampleWithKey, SampleWithKey.Builder> metaClass() {
        return new Meta();
    }

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleWithKey create(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text,
            @JsonProperty("number") int number) {
        return SampleWithKey.builder()
                .id(id)
                .text(text)
                .number(number)
                .build();
    }

    @Override
    @Key
    public abstract String id();

    @Override
    public abstract String text();

    @Override
    public abstract int number();

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
}
