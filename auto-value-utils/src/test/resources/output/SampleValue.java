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
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleValue implements SampleValuePrototype, HasMetaClass<com.slimgears.sample.SampleValue> {

    @Override
    public MetaClass<SampleValue> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleValue> {
        private final TypeToken<SampleValue> objectClass = new TypeToken<SampleValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleValue, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleValue, Double> doubleValue = PropertyMeta.<SampleValue, Double, Builder>create(this, "doubleValue", new TypeToken<Double>(){}, SampleValue::doubleValue, Builder::doubleValue);
        public final PropertyMeta<SampleValue, Boolean> foo = PropertyMeta.<SampleValue, Boolean, Builder>create(this, "foo", new TypeToken<Boolean>(){}, SampleValue::foo, Builder::foo);
        public final PropertyMeta<SampleValue, Integer> intValue = PropertyMeta.<SampleValue, Integer, Builder>create(this, "intValue", new TypeToken<Integer>(){}, SampleValue::intValue, Builder::intValue);
        public final PropertyMeta<SampleValue, String> strValue = PropertyMeta.<SampleValue, String, Builder>create(this, "strValue", new TypeToken<String>(){}, SampleValue::strValue, Builder::strValue);

        Meta() {
            propertyMap.put("doubleValue", doubleValue);
            propertyMap.put("foo", foo);
            propertyMap.put("intValue", intValue);
            propertyMap.put("strValue", strValue);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleValue> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleValue, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleValue, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleValue, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleValue, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleValue.builder();
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

    public static SampleValue create(
         double doubleValue,
         boolean foo,
         int intValue,
         String strValue) {
        return SampleValue.builder()
            .doubleValue(doubleValue)
            .foo(foo)
            .intValue(intValue)
            .strValue(strValue)
            .build();
    }

    public static SampleValue create(
        double doubleValue,
        boolean foo,
        int intValue) {
        return SampleValue.builder()
            .doubleValue(doubleValue)
            .foo(foo)
            .intValue(intValue)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValue, Builder>, SampleValuePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleValue.Builder();
        }

        @Override
        @SampleFieldAnnotation(strValue = "test")
        Builder doubleValue(double doubleValue);

        @Override
        Builder foo(boolean foo);

        @Override
        Builder intValue(int intValue);

        @Override
        @SampleFieldAnnotation
        Builder strValue(String strValue);
    }

    @Override
    @SampleFieldAnnotation(strValue = "test")
    public abstract double doubleValue();

    @Override
    public abstract boolean foo();

    @Override
    public abstract int intValue();

    @Override
    @SampleFieldAnnotation
    @Nullable
    public abstract String strValue();

}
