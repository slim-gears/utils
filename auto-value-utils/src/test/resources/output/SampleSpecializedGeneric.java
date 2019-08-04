package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
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
public abstract class SampleSpecializedGeneric implements SampleSpecializedGenericPrototype, HasMetaClass<SampleSpecializedGeneric> {

    @Override
    
    public MetaClass<SampleSpecializedGeneric> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleSpecializedGeneric> {
        private final TypeToken<SampleSpecializedGeneric> objectType = new TypeToken<SampleSpecializedGeneric>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleSpecializedGeneric, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleSpecializedGeneric, ImmutableList<String>> values = PropertyMeta.<SampleSpecializedGeneric, ImmutableList<String>, Builder>create(this, "values", new PropertyType<ImmutableList<String>>(){}, obj -> obj.values(), Builder::values);

        Meta() {
            propertyMap.put("values", values);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleSpecializedGeneric> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleSpecializedGeneric, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleSpecializedGeneric, __V> getProperty(String name) {
            return (PropertyMeta<SampleSpecializedGeneric, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleSpecializedGeneric>> B createBuilder() {
            return (B)(BuilderPrototype)SampleSpecializedGeneric.builder();
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

    public static SampleSpecializedGeneric create(
         ImmutableList<String> values) {
        return SampleSpecializedGeneric.builder()
            .values(values)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleSpecializedGeneric, Builder>, SampleSpecializedGenericPrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleSpecializedGeneric.Builder();
        }

        @Override
        Builder values(ImmutableList<String> values);
            ImmutableList.Builder<String> valuesBuilder();

    }

    @Override public abstract ImmutableList<String> values();

}
