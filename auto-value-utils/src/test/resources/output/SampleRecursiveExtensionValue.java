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
public abstract class SampleRecursiveExtensionValue implements SampleRecursiveExtensionValuePrototype, HasMetaClass<SampleRecursiveExtensionValue> {

    public static SampleRecursiveExtensionValue create(
         int intValue) {
        return SampleRecursiveExtensionValue.builder()
            .intValue(intValue)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleRecursiveExtensionValue, Builder>, SampleRecursiveExtensionValuePrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleRecursiveExtensionValue.Builder();
        }

        @Override
        Builder intValue(int intValue);
    }

    @Override
    
    public MetaClass<SampleRecursiveExtensionValue> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleRecursiveExtensionValue> {
        private final TypeToken<SampleRecursiveExtensionValue> objectType = new TypeToken<SampleRecursiveExtensionValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleRecursiveExtensionValue, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleRecursiveExtensionValue, Integer> intValue = PropertyMeta.<SampleRecursiveExtensionValue, Integer, Builder>create(this, "intValue", new PropertyType<Integer>(){}, obj -> obj.intValue(), Builder::intValue);

        Meta() {
            propertyMap.put("intValue", intValue);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleRecursiveExtensionValue> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleRecursiveExtensionValue, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleRecursiveExtensionValue, __V> getProperty(String name) {
            return (PropertyMeta<SampleRecursiveExtensionValue, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleRecursiveExtensionValue>> B createBuilder() {
            return (B)(BuilderPrototype)SampleRecursiveExtensionValue.builder();
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

    @Override public abstract int intValue();

}
