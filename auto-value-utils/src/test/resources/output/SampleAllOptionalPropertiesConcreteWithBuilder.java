package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
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
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleAllOptionalPropertiesConcreteWithBuilder implements SampleAllOptionalPropertiesProto, HasMetaClass<SampleAllOptionalPropertiesConcreteWithBuilder> {

    @Override
    @JsonIgnore
    public MetaClass<SampleAllOptionalPropertiesConcreteWithBuilder> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleAllOptionalPropertiesConcreteWithBuilder> {
        private final TypeToken<SampleAllOptionalPropertiesConcreteWithBuilder> objectType = new TypeToken<SampleAllOptionalPropertiesConcreteWithBuilder>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, Integer> intProperty = PropertyMeta.<SampleAllOptionalPropertiesConcreteWithBuilder, Integer, Builder>create(this, "intProperty", new PropertyType<Integer>(){}, obj -> obj.intProperty(), Builder::intProperty);
        public final PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, String> strProperty = PropertyMeta.<SampleAllOptionalPropertiesConcreteWithBuilder, String, Builder>create(this, "strProperty", new PropertyType<String>(){}, obj -> obj.strProperty(), Builder::strProperty);

        Meta() {
            propertyMap.put("intProperty", intProperty);
            propertyMap.put("strProperty", strProperty);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleAllOptionalPropertiesConcreteWithBuilder> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, __V> getProperty(String name) {
            return (PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleAllOptionalPropertiesConcreteWithBuilder>> B createBuilder() {
            return (B)(BuilderPrototype)SampleAllOptionalPropertiesConcreteWithBuilder.builder();
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

    @JsonCreator
    public static SampleAllOptionalPropertiesConcreteWithBuilder create(
        @Nullable @JsonProperty("intProperty") Integer intProperty,
        @Nullable @JsonProperty("strProperty") String strProperty) {
        return SampleAllOptionalPropertiesConcreteWithBuilder.builder()
            .intProperty(intProperty)
            .strProperty(strProperty)
            .build();
    }

    public static SampleAllOptionalPropertiesConcreteWithBuilder create() {
        return SampleAllOptionalPropertiesConcreteWithBuilder.builder()
            .build();
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleAllOptionalPropertiesConcreteWithBuilder, Builder>, SampleAllOptionalPropertiesProtoBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleAllOptionalPropertiesConcreteWithBuilder.Builder();
        }

        @Override
        Builder intProperty(Integer intProperty);

        @Override
        Builder strProperty(String strProperty);
    }

    public static SampleAllOptionalPropertiesConcreteWithBuilder fromIntProperty(Integer intProperty) {
        return SampleAllOptionalPropertiesConcreteWithBuilder.builder().intProperty(intProperty).build();
    }

    public static SampleAllOptionalPropertiesConcreteWithBuilder fromStrProperty(String strProperty) {
        return SampleAllOptionalPropertiesConcreteWithBuilder.builder().strProperty(strProperty).build();
    }

    @Override @Nullable @JsonProperty("intProperty") public abstract Integer intProperty();
    @Override @Nullable @JsonProperty("strProperty") public abstract String strProperty();

}
