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
public abstract class ValueFilter<T> implements ValueFilterPrototype<T>, HasMetaClass<ValueFilter<T>> {

    @Override
    
    public MetaClass<ValueFilter<T>> metaClass() {
        return (MetaClass<ValueFilter<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<ValueFilter<T>> {
        private final TypeToken<ValueFilter<T>> objectType = new TypeToken<ValueFilter<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<ValueFilter<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<ValueFilter<T>, Boolean> isNull = PropertyMeta.<ValueFilter<T>, Boolean, Builder<T>>create(this, "isNull", new TypeToken<Boolean>(){}, obj -> obj.isNull(), Builder::isNull);
        public final PropertyMeta<ValueFilter<T>, T> equalsTo = PropertyMeta.<ValueFilter<T>, T, Builder<T>>create(this, "equalsTo", new TypeToken<T>(){}, obj -> obj.equalsTo(), Builder::equalsTo);

        Meta() {
            propertyMap.put("isNull", isNull);
            propertyMap.put("equalsTo", equalsTo);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<ValueFilter<T>> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<ValueFilter<T>, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<ValueFilter<T>, __V> getProperty(String name) {
            return (PropertyMeta<ValueFilter<T>, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<ValueFilter<T>>> B createBuilder() {
            return (B)(BuilderPrototype)ValueFilter.builder();
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

    public static <T> ValueFilter<T> create(
         Boolean isNull,
         T equalsTo) {
        return ValueFilter.<T>builder()
            .isNull(isNull)
            .equalsTo(equalsTo)
            .build();
    }

    public static <T> ValueFilter<T> create() {
        return ValueFilter.<T>builder()
            .build();
    }

    public abstract Builder<T> toBuilder();

    public static <T> Builder<T> builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder<T> extends BuilderPrototype<ValueFilter<T>, Builder<T>>, ValueFilterPrototypeBuilder<T, Builder<T>>{
        public static <T> Builder<T> create() {
            return new AutoValue_ValueFilter.Builder<>();
        }

        @Override
        Builder<T> isNull(Boolean isNull);

        @Override
        Builder<T> equalsTo(T equalsTo);
    }

    public static <T> ValueFilter<T> fromIsNull(Boolean isNull) {
        return ValueFilter.<T>builder().isNull(isNull).build();
    }

    public static <T> ValueFilter<T> fromEqualsTo(T equalsTo) {
        return ValueFilter.<T>builder().equalsTo(equalsTo).build();
    }

    @Override public abstract Boolean isNull();
    @Override public abstract T equalsTo();

}
