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
public abstract class SampleComparableGeneric<T extends Comparable<T>> implements SampleComparableGenericPrototype<T>, HasMetaClass<com.slimgears.sample.SampleComparableGeneric<T>> {

    @Override
    public MetaClass<SampleComparableGeneric<T>> metaClass() {
        return (MetaClass<SampleComparableGeneric<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T extends Comparable<T>> implements MetaClass<SampleComparableGeneric<T>> {
        private final TypeToken<SampleComparableGeneric<T>> objectClass = new TypeToken<SampleComparableGeneric<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<SampleComparableGeneric<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleComparableGeneric<T>, T> tValue = PropertyMeta.<SampleComparableGeneric<T>, T, Builder<T>>create(this, "tValue", new TypeToken<T>(){}, SampleComparableGeneric::tValue, Builder::tValue);

        Meta() {
            propertyMap.put("tValue", tValue);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleComparableGeneric<T>> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleComparableGeneric<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleComparableGeneric<T>, __V> getProperty(String name) {
            return (PropertyMeta<SampleComparableGeneric<T>, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleComparableGeneric<T>, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleComparableGeneric.builder();
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

    public static <T extends Comparable<T>> SampleComparableGeneric<T> create(
         T tValue) {
        return SampleComparableGeneric.<T>builder()
            .tValue(tValue)
            .build();
    }

    public abstract Builder<T> toBuilder();

    public static <T extends Comparable<T>> Builder<T> builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder<T extends Comparable<T>> extends BuilderPrototype<SampleComparableGeneric<T>, Builder<T>>, SampleComparableGenericPrototypeBuilder<T, Builder<T>> {
        public static <T extends Comparable<T>> Builder<T> create() {
            return new AutoValue_SampleComparableGeneric.Builder<>();
        }

        @Override
        Builder<T> tValue(T tValue);
    }

    @Override public abstract T tValue();

}
