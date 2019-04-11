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
public abstract class SampleValue implements SampleValuePrototype, HasMetaClass<SampleValue> {

    @Override
    public MetaClass<SampleValue> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleValue> {
        private final TypeToken<SampleValue> objectClass = new TypeToken<SampleValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleValue, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleValue, Integer> intValue = PropertyMeta.<SampleValue, Integer, Builder>create(this, "intValue", new TypeToken<Integer>(){}, obj -> obj.intValue(), Builder::intValue);
        public final PropertyMeta<SampleValue, Double> doubleValue = PropertyMeta.<SampleValue, Double, Builder>create(this, "doubleValue", new TypeToken<Double>(){}, obj -> obj.doubleValue(), Builder::doubleValue);
        public final PropertyMeta<SampleValue, String> strValue = PropertyMeta.<SampleValue, String, Builder>create(this, "strValue", new TypeToken<String>(){}, obj -> obj.strValue(), Builder::strValue);
        public final PropertyMeta<SampleValue, Boolean> foo = PropertyMeta.<SampleValue, Boolean, Builder>create(this, "foo", new TypeToken<Boolean>(){}, obj -> obj.foo(), Builder::foo);

        Meta() {
            propertyMap.put("intValue", intValue);
            propertyMap.put("doubleValue", doubleValue);
            propertyMap.put("strValue", strValue);
            propertyMap.put("foo", foo);
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
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleValue, __V> getProperty(String name) {
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
         int intValue,
         double doubleValue,
         String strValue,
         boolean foo) {
        return SampleValue.builder()
            .intValue(intValue)
            .doubleValue(doubleValue)
            .strValue(strValue)
            .foo(foo)
            .build();
    }

    public static SampleValue create(
int intValue,
double doubleValue,
boolean foo) {
        return SampleValue.builder()
            .intValue(intValue)
            .doubleValue(doubleValue)
            .foo(foo)
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
        Builder intValue(int intValue);

        @Override
        @SampleFieldAnnotation(strValue = "test")
        Builder doubleValue(double doubleValue);

        @Override
        @SampleFieldAnnotation
        Builder strValue(String strValue);

        @Override
        Builder foo(boolean foo);
    }

    @Override public abstract int intValue();
    @Override public abstract double doubleValue();
    @Override public abstract String strValue();
    @Override public abstract boolean foo();

    public static SampleValue createDefault() {
        return SampleValuePrototype.createDefault();
    }
}
