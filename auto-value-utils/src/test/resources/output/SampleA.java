package com.slimgears.sample.a;

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
public abstract class SampleA implements SampleAPrototype, HasMetaClass<com.slimgears.sample.a.SampleA> {

    @Override
    public MetaClass<SampleA> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleA> {
        private final TypeToken<SampleA> objectClass = new TypeToken<SampleA>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleA, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleA, Integer> value = PropertyMeta.<SampleA, Integer, Builder>create(this, "value", new TypeToken<Integer>(){}, SampleA::value, Builder::value);

        Meta() {
            propertyMap.put("value", value);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleA> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleA, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleA, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleA, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleA, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleA.builder();
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

    public static SampleA create(
         int value) {
        return SampleA.builder()
            .value(value)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleA, Builder>, SampleAPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleA.Builder();
        }

        @Override
        Builder value(int value);
    }

    @Override
    public abstract int value();

}
