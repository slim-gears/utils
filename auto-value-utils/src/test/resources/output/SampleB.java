package com.slimgears.sample.b;

import com.google.auto.value.AutoValue;
import com.slimgears.sample.a.SampleA;
import com.slimgears.sample.a.SampleAPrototype;
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
public abstract class SampleB implements SampleBPrototype, HasMetaClass<SampleB> {

    @Override
    public MetaClass<SampleB> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleB> {
        private final TypeToken<SampleB> objectClass = new TypeToken<SampleB>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleB, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleB, SampleA> value = PropertyMeta.<SampleB, SampleA, Builder>create(this, "value", new TypeToken<SampleA>(){}, obj -> obj.value(), Builder::value);
        public final PropertyMeta<SampleB, SampleAPrototype.NestedEnum> nestedEnum = PropertyMeta.<SampleB, SampleAPrototype.NestedEnum, Builder>create(this, "nestedEnum", new TypeToken<SampleAPrototype.NestedEnum>(){}, obj -> obj.nestedEnum(), Builder::nestedEnum);

        Meta() {
            propertyMap.put("value", value);
            propertyMap.put("nestedEnum", nestedEnum);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleB> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleB, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleB, __V> getProperty(String name) {
            return (PropertyMeta<SampleB, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends BuilderPrototype<SampleB, B>> B createBuilder() {
            return (B)(BuilderPrototype)SampleB.builder();
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

    public static SampleB create(
         SampleA value,
         SampleAPrototype.NestedEnum nestedEnum) {
        return SampleB.builder()
            .value(value)
            .nestedEnum(nestedEnum)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleB, Builder>, SampleBPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleB.Builder();
        }

        @Override
        Builder value(SampleA value);
            SampleA.Builder valueBuilder();

        @Override
        Builder nestedEnum(SampleAPrototype.NestedEnum nestedEnum);
    }

    @Override public abstract SampleA value();
    @Override public abstract SampleAPrototype.NestedEnum nestedEnum();

}
