package com.slimgears.sample;

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
public abstract class SampleNestedType implements SampleNestedTypePrototype, HasMetaClass<SampleNestedType> {

    @Override
    
    public MetaClass<SampleNestedType> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleNestedType> {
        private final TypeToken<SampleNestedType> objectType = new TypeToken<SampleNestedType>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleNestedType, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleNestedType, SampleNestedTypePrototype.NestedEnum> value = PropertyMeta.<SampleNestedType, SampleNestedTypePrototype.NestedEnum, Builder>create(this, "value", new PropertyType<SampleNestedTypePrototype.NestedEnum>(){}, obj -> obj.value(), Builder::value);

        Meta() {
            propertyMap.put("value", value);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleNestedType> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleNestedType, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleNestedType, __V> getProperty(String name) {
            return (PropertyMeta<SampleNestedType, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleNestedType>> B createBuilder() {
            return (B)(BuilderPrototype)SampleNestedType.builder();
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

    public static SampleNestedType create(
         SampleNestedTypePrototype.NestedEnum value) {
        return SampleNestedType.builder()
            .value(value)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleNestedType, Builder>, SampleNestedTypePrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleNestedType.Builder();
        }

        @Override
        Builder value(SampleNestedTypePrototype.NestedEnum value);
    }

    @Override public abstract SampleNestedTypePrototype.NestedEnum value();

}
