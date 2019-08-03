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
public abstract class ObjectFilter<T> implements ObjectFilterPrototype<T>, HasMetaClass<ObjectFilter<T>> {

    @Override
    
    public MetaClass<ObjectFilter<T>> metaClass() {
        return (MetaClass<ObjectFilter<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<ObjectFilter<T>> {
        private final TypeToken<ObjectFilter<T>> objectType = new TypeToken<ObjectFilter<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<ObjectFilter<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<ObjectFilter<T>, Boolean> isNull = PropertyMeta.<ObjectFilter<T>, Boolean, Builder<T>>create(this, "isNull", new TypeToken<Boolean>(){}, obj -> obj.isNull(), Builder::isNull);

        Meta() {
            propertyMap.put("isNull", isNull);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<ObjectFilter<T>> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<ObjectFilter<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<ObjectFilter<T>, __V> getProperty(String name) {
            return (PropertyMeta<ObjectFilter<T>, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<ObjectFilter<T>>> B createBuilder() {
            return (B)(BuilderPrototype)ObjectFilter.builder();
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

    public static <T> ObjectFilter<T> create(
         Boolean isNull) {
        return ObjectFilter.<T>builder()
            .isNull(isNull)
            .build();
    }

    public static <T> ObjectFilter<T> create() {
        return ObjectFilter.<T>builder()
            .build();
    }

    public abstract Builder<T> toBuilder();

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<ObjectFilter<T>, Builder<T>>, ObjectFilterPrototypeBuilder<T, Builder<T>>{
        public static <T> Builder<T> create() {
            return new AutoValue_ObjectFilter.Builder<>();
        }

        @Override
        Builder<T> isNull(Boolean isNull);
    }

    public static <T> ObjectFilter<T> fromIsNull(Boolean isNull) {
        return ObjectFilter.<T>builder().isNull(isNull).build();
    }

    @Override public abstract Boolean isNull();

}
