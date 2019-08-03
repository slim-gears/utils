package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.google.common.reflect.TypeToken;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaBuilder;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleCircularDependency implements SampleCircularDependencyPrototype, HasMetaClass<SampleCircularDependency> {

    @Override
    
    public MetaClass<SampleCircularDependency> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleCircularDependency> {
        private final TypeToken<SampleCircularDependency> objectType = new TypeToken<SampleCircularDependency>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleCircularDependency, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleCircularDependency, SampleCircularDependency> parent = PropertyMeta.<SampleCircularDependency, SampleCircularDependency, Builder>create(this, "parent", new TypeToken<SampleCircularDependency>(){}, obj -> obj.parent(), Builder::parent);

        Meta() {
            propertyMap.put("parent", parent);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleCircularDependency> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleCircularDependency, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleCircularDependency, __V> getProperty(String name) {
            return (PropertyMeta<SampleCircularDependency, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleCircularDependency>> B createBuilder() {
            return (B)(BuilderPrototype)SampleCircularDependency.builder();
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

    public static SampleCircularDependency create(
         SampleCircularDependency parent) {
        return SampleCircularDependency.builder()
            .parent(parent)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleCircularDependency, Builder>, SampleCircularDependencyPrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleCircularDependency.Builder();
        }

        @Override
        Builder parent(SampleCircularDependency parent);
    }

    @Override public abstract SampleCircularDependency parent();

}
