package com.slimgears.sample;

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
public abstract class ObjectFilter<T> implements ObjectFilterPrototype<T>, HasMetaClass<ObjectFilter<T>> {

    @Override
    
    public MetaClass<ObjectFilter<T>> metaClass() {
        return (MetaClass<ObjectFilter<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<ObjectFilter<T>> {
        private final TypeToken<ObjectFilter<T>> objectClass = new TypeToken<ObjectFilter<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<ObjectFilter<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<ObjectFilter<T>, Boolean> _null = PropertyMeta.<ObjectFilter<T>, Boolean, Builder<T>>create(this, "null", new TypeToken<Boolean>(){}, obj -> obj.isNull(), Builder::setNull, "isNull");

        Meta() {
            propertyMap.put("null", _null);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<ObjectFilter<T>> objectClass() {
            return this.objectClass;
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
        public <B extends BuilderPrototype<ObjectFilter<T>, B>> B createBuilder() {
            return (B)(BuilderPrototype)ObjectFilter.builder();
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

    public static <T> ObjectFilter<T> create(
         Boolean _null) {
        return ObjectFilter.<T>builder()
            .null(_null)
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
    public interface Builder<T> extends BuilderPrototype<ObjectFilter<T>, Builder<T>>, ObjectFilterPrototypeBuilder<T, Builder<T>> {
        public static <T> Builder<T> create() {
            return new AutoValue_ObjectFilter.Builder<>();
        }

        @Override
        Builder<T> setNull(Boolean _null);
    }

    public static <T> ObjectFilter<T> fromNull(Boolean _null) {
        return ObjectFilter.<T>builder().setNull(_null).build();
    }

    @Override public abstract Boolean isNull();

}
