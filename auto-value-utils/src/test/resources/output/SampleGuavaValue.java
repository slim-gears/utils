package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
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
public abstract class SampleGuavaValue implements SampleGuavaValuePrototype, HasMetaClass<com.slimgears.sample.SampleGuavaValue> {

    public MetaClass<SampleGuavaValue> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleGuavaValue> {
        private final TypeToken<SampleGuavaValue> objectClass = new TypeToken<SampleGuavaValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleGuavaValue, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleGuavaValue, ImmutableList<Integer>> intList = PropertyMeta.<SampleGuavaValue, ImmutableList<Integer>, Builder>create(this, "intList", new TypeToken<ImmutableList<Integer>>(){}, SampleGuavaValue::intList, Builder::intList);
        public final PropertyMeta<SampleGuavaValue, ImmutableBiMap<Integer, String>> intToStringBiMap = PropertyMeta.<SampleGuavaValue, ImmutableBiMap<Integer, String>, Builder>create(this, "intToStringBiMap", new TypeToken<ImmutableBiMap<Integer, String>>(){}, SampleGuavaValue::intToStringBiMap, Builder::intToStringBiMap);
        public final PropertyMeta<SampleGuavaValue, ImmutableMap<Integer, String>> intToStringMap = PropertyMeta.<SampleGuavaValue, ImmutableMap<Integer, String>, Builder>create(this, "intToStringMap", new TypeToken<ImmutableMap<Integer, String>>(){}, SampleGuavaValue::intToStringMap, Builder::intToStringMap);
        public final PropertyMeta<SampleGuavaValue, ImmutableList<String>> optionalList = PropertyMeta.<SampleGuavaValue, ImmutableList<String>, Builder>create(this, "optionalList", new TypeToken<ImmutableList<String>>(){}, SampleGuavaValue::optionalList, Builder::optionalList);
        public final PropertyMeta<SampleGuavaValue, ImmutableSet<String>> stringSet = PropertyMeta.<SampleGuavaValue, ImmutableSet<String>, Builder>create(this, "stringSet", new TypeToken<ImmutableSet<String>>(){}, SampleGuavaValue::stringSet, Builder::stringSet);

        Meta() {
            propertyMap.put("intList", intList);
            propertyMap.put("intToStringBiMap", intToStringBiMap);
            propertyMap.put("intToStringMap", intToStringMap);
            propertyMap.put("optionalList", optionalList);
            propertyMap.put("stringSet", stringSet);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleGuavaValue> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleGuavaValue, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleGuavaValue, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleGuavaValue, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleGuavaValue.builder();
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

    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleGuavaValue create(
            @JsonProperty("intList") ImmutableList<Integer> intList,
            @JsonProperty("intToStringBiMap") ImmutableBiMap<Integer, String> intToStringBiMap,
            @JsonProperty("intToStringMap") ImmutableMap<Integer, String> intToStringMap,
            @JsonProperty("optionalList") ImmutableList<String> optionalList,
            @JsonProperty("stringSet") ImmutableSet<String> stringSet) {
        return SampleGuavaValue.builder()
                .intList(intList)
                .intToStringBiMap(intToStringBiMap)
                .intToStringMap(intToStringMap)
                .optionalList(optionalList)
                .stringSet(stringSet)
                .build();
    }

    @Override
    public abstract ImmutableList<Integer> intList();

    @Override
    public abstract ImmutableBiMap<Integer, String> intToStringBiMap();

    @Override
    public abstract ImmutableMap<Integer, String> intToStringMap();

    @Override
    @Nullable
    public abstract ImmutableList<String> optionalList();

    @Override
    public abstract ImmutableSet<String> stringSet();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleGuavaValue, Builder>, SampleGuavaValuePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleGuavaValue.Builder();
        }

        @Override
        Builder intList(ImmutableList<Integer> intList);
        ImmutableList.Builder<Integer> intListBuilder();

        @Override
        Builder intToStringBiMap(ImmutableBiMap<Integer, String> intToStringBiMap);
        ImmutableBiMap.Builder<Integer, String> intToStringBiMapBuilder();

        @Override
        Builder intToStringMap(ImmutableMap<Integer, String> intToStringMap);
        ImmutableMap.Builder<Integer, String> intToStringMapBuilder();

        @Override
        Builder optionalList(ImmutableList<String> optionalList);

        @Override
        Builder stringSet(ImmutableSet<String> stringSet);
        ImmutableSet.Builder<String> stringSetBuilder();

    }
}
