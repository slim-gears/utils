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
public abstract class SampleAllOptionalPropertiesConcreteWithBuilder implements SampleAllOptionalPropertiesProto, HasMetaClass<com.slimgears.sample.SampleAllOptionalPropertiesConcreteWithBuilder> {

    @Override
    public MetaClass<SampleAllOptionalPropertiesConcreteWithBuilder> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleAllOptionalPropertiesConcreteWithBuilder> {
        private final TypeToken<SampleAllOptionalPropertiesConcreteWithBuilder> objectClass = new TypeToken<SampleAllOptionalPropertiesConcreteWithBuilder>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, Integer> intProperty = PropertyMeta.<SampleAllOptionalPropertiesConcreteWithBuilder, Integer, Builder>create(this, "intProperty", new TypeToken<Integer>(){}, SampleAllOptionalPropertiesConcreteWithBuilder::intProperty, Builder::intProperty);
        public final PropertyMeta<SampleAllOptionalPropertiesConcreteWithBuilder, String> strProperty = PropertyMeta.<SampleAllOptionalPropertiesConcreteWithBuilder, String, Builder>create(this, "strProperty", new TypeToken<String>(){}, SampleAllOptionalPropertiesConcreteWithBuilder::strProperty, Builder::strProperty);

        Meta() {
            propertyMap.put("intProperty", intProperty);
            propertyMap.put("strProperty", strProperty);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleAllOptionalPropertiesConcreteWithBuilder> objectClass() {
            return this.objectClass;
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
        public <B extends BuilderPrototype<SampleAllOptionalPropertiesConcreteWithBuilder, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleAllOptionalPropertiesConcreteWithBuilder.builder();
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

    @JsonCreator
    public static SampleAllOptionalPropertiesConcreteWithBuilder create(
        @JsonProperty("intProperty") Integer intProperty,
        @JsonProperty("strProperty") String strProperty) {
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
    public interface Builder extends BuilderPrototype<SampleAllOptionalPropertiesConcreteWithBuilder, Builder>, SampleAllOptionalPropertiesProtoBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleAllOptionalPropertiesConcreteWithBuilder.Builder();
        }

        @Override
        Builder intProperty(Integer intProperty);

        @Override
        Builder strProperty(String strProperty);
    }

    public static SampleAllOptionalPropertiesConcreteWithBuilder intProperty(Integer intProperty) {
        return SampleAllOptionalPropertiesConcreteWithBuilder.builder().intProperty(intProperty).build();
    }

    public static SampleAllOptionalPropertiesConcreteWithBuilder strProperty(String strProperty) {
        return SampleAllOptionalPropertiesConcreteWithBuilder.builder().strProperty(strProperty).build();
    }

    @Override @JsonProperty public abstract Integer intProperty();
    @Override @JsonProperty public abstract String strProperty();

}
