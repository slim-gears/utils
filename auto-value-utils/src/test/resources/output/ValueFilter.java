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
public abstract class ValueFilter<T> implements ValueFilterPrototype<T>, HasMetaClass<ValueFilter<T>> {

    @Override
    
    public MetaClass<ValueFilter<T>> metaClass() {
        return (MetaClass<ValueFilter<T>>)metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta<T> implements MetaClass<ValueFilter<T>> {
        private final TypeToken<ValueFilter<T>> objectClass = new TypeToken<ValueFilter<T>>(){};
        private final TypeToken<Builder<T>> builderClass = new TypeToken<Builder<T>>(){};
        private final Map<String, PropertyMeta<ValueFilter<T>, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<ValueFilter<T>, Boolean> _null = PropertyMeta.<ValueFilter<T>, Boolean, Builder<T>>create(this, "null", new TypeToken<Boolean>(){}, obj -> obj.isNull(), Builder::setNull, "isNull");

        Meta() {
            propertyMap.put("null", _null);
        }

        @Override
        public TypeToken<Builder<T>> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<ValueFilter<T>> objectClass() {
            return this.objectClass;
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
        public <B extends BuilderPrototype<ValueFilter<T>, B>> B createBuilder() {
            return (B)(BuilderPrototype)ValueFilter.builder();
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

    public static <T> ValueFilter<T> create(
         Boolean _null) {
        return ValueFilter.<T>builder()
            .null(_null)
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
    public interface Builder<T> extends BuilderPrototype<ValueFilter<T>, Builder<T>>, ValueFilterPrototypeBuilder<T, Builder<T>> {
        public static <T> Builder<T> create() {
            return new AutoValue_ValueFilter.Builder<>();
        }

        @Override
        Builder<T> setNull(Boolean _null);
    }

    public static <T> ValueFilter<T> fromNull(Boolean _null) {
        return ValueFilter.<T>builder().setNull(_null).build();
    }

    @Override public abstract Boolean isNull();

}
