package com.slimgears.sample;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.google.common.reflect.TypeToken;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.MetaBuilder;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.annotations.PropertyType;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleGuavaValue implements SampleGuavaValuePrototype, HasMetaClass<SampleGuavaValue> {

    @Override
    
    public MetaClass<SampleGuavaValue> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleGuavaValue> {
        private final TypeToken<SampleGuavaValue> objectType = new TypeToken<SampleGuavaValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleGuavaValue, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleGuavaValue, ImmutableList<Integer>> intList = PropertyMeta.<SampleGuavaValue, ImmutableList<Integer>, Builder>create(this, "intList", new PropertyType<ImmutableList<Integer>>(){}, obj -> obj.intList(), Builder::intList);
        public final PropertyMeta<SampleGuavaValue, ImmutableSet<String>> stringSet = PropertyMeta.<SampleGuavaValue, ImmutableSet<String>, Builder>create(this, "stringSet", new PropertyType<ImmutableSet<String>>(){}, obj -> obj.stringSet(), Builder::stringSet);
        public final PropertyMeta<SampleGuavaValue, ImmutableMap<Integer, String>> intToStringMap = PropertyMeta.<SampleGuavaValue, ImmutableMap<Integer, String>, Builder>create(this, "intToStringMap", new PropertyType<ImmutableMap<Integer, String>>(){}, obj -> obj.intToStringMap(), Builder::intToStringMap);
        public final PropertyMeta<SampleGuavaValue, ImmutableBiMap<Integer, String>> intToStringBiMap = PropertyMeta.<SampleGuavaValue, ImmutableBiMap<Integer, String>, Builder>create(this, "intToStringBiMap", new PropertyType<ImmutableBiMap<Integer, String>>(){}, obj -> obj.intToStringBiMap(), Builder::intToStringBiMap);
        public final PropertyMeta<SampleGuavaValue, ImmutableList<String>> optionalList = PropertyMeta.<SampleGuavaValue, ImmutableList<String>, Builder>create(this, "optionalList", new PropertyType<ImmutableList<String>>(){}, obj -> obj.optionalList(), Builder::optionalList);

        Meta() {
            propertyMap.put("intList", intList);
            propertyMap.put("stringSet", stringSet);
            propertyMap.put("intToStringMap", intToStringMap);
            propertyMap.put("intToStringBiMap", intToStringBiMap);
            propertyMap.put("optionalList", optionalList);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleGuavaValue> asType() {
            return this.objectType;
        }

        @Override
        public Iterable<PropertyMeta<SampleGuavaValue, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        @SuppressWarnings("unchecked")
        public <__V> PropertyMeta<SampleGuavaValue, __V> getProperty(String name) {
            return (PropertyMeta<SampleGuavaValue, __V>)propertyMap.get(name);
        }

        @Override
        public <B extends MetaBuilder<SampleGuavaValue>> B createBuilder() {
            return (B)(BuilderPrototype)SampleGuavaValue.builder();
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

    public static SampleGuavaValue create(
         ImmutableList<Integer> intList,
         ImmutableSet<String> stringSet,
         ImmutableMap<Integer, String> intToStringMap,
         ImmutableBiMap<Integer, String> intToStringBiMap,
         ImmutableList<String> optionalList) {
        return SampleGuavaValue.builder()
            .intList(intList)
            .stringSet(stringSet)
            .intToStringMap(intToStringMap)
            .intToStringBiMap(intToStringBiMap)
            .optionalList(optionalList)
            .build();
    }

    public static SampleGuavaValue create(
        ImmutableList<Integer> intList,
        ImmutableSet<String> stringSet,
        ImmutableMap<Integer, String> intToStringMap,
        ImmutableBiMap<Integer, String> intToStringBiMap) {
        return SampleGuavaValue.builder()
            .intList(intList)
            .stringSet(stringSet)
            .intToStringMap(intToStringMap)
            .intToStringBiMap(intToStringBiMap)
            .build();
    }

    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleGuavaValue, Builder>, SampleGuavaValuePrototypeBuilder<Builder>{
        public static Builder create() {
            return new AutoValue_SampleGuavaValue.Builder();
        }

        @Override
        Builder intList(ImmutableList<Integer> intList);
            ImmutableList.Builder<Integer> intListBuilder();

        @Override
        Builder stringSet(ImmutableSet<String> stringSet);
            ImmutableSet.Builder<String> stringSetBuilder();

        @Override
        Builder intToStringMap(ImmutableMap<Integer, String> intToStringMap);
            ImmutableMap.Builder<Integer, String> intToStringMapBuilder();

        @Override
        Builder intToStringBiMap(ImmutableBiMap<Integer, String> intToStringBiMap);
            ImmutableBiMap.Builder<Integer, String> intToStringBiMapBuilder();

        @Override
        Builder optionalList(ImmutableList<String> optionalList);
    }

    @Override public abstract ImmutableList<Integer> intList();
    @Override public abstract ImmutableSet<String> stringSet();
    @Override public abstract ImmutableMap<Integer, String> intToStringMap();
    @Override public abstract ImmutableBiMap<Integer, String> intToStringBiMap();
    @Override public abstract ImmutableList<String> optionalList();

}
