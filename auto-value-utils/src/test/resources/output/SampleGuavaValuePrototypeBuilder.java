package com.slimgears.sample;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
public interface SampleGuavaValuePrototypeBuilder<_B extends SampleGuavaValuePrototypeBuilder<_B>> {

    _B intList(ImmutableList<Integer> intList);

    ImmutableList.Builder<Integer> intListBuilder();

    default _B addToIntList(Integer element) {
        intListBuilder().add(element);
        return (_B)this;
    }

    default _B addToIntList(Integer... elements) {
        intListBuilder().add(elements);
        return (_B)this;
    }

    default _B addAllToIntList(Iterable<? extends Integer> elements) {
        intListBuilder().addAll(elements);
        return (_B)this;
    }

    default _B addAllToIntList(Iterator<? extends Integer> elements) {
        intListBuilder().addAll(elements);
        return (_B)this;
    }

    _B stringSet(ImmutableSet<String> stringSet);

    ImmutableSet.Builder<String> stringSetBuilder();

    default _B addToStringSet(String element) {
        stringSetBuilder().add(element);
        return (_B)this;
    }

    default _B addToStringSet(String... elements) {
        stringSetBuilder().add(elements);
        return (_B)this;
    }

    default _B addAllToStringSet(Iterable<? extends String> elements) {
        stringSetBuilder().addAll(elements);
        return (_B)this;
    }

    default _B addAllToStringSet(Iterator<? extends String> elements) {
        stringSetBuilder().addAll(elements);
        return (_B)this;
    }

    _B intToStringMap(ImmutableMap<Integer, String> intToStringMap);

    ImmutableMap.Builder<Integer, String> intToStringMapBuilder();

    default _B putToIntToStringMap(Integer key, String value) {
        intToStringMapBuilder().put(key, value);
        return (_B)this;
    }

    default _B putToIntToStringMap(Map.Entry<? extends Integer, ? extends String> entry) {
        intToStringMapBuilder().put(entry);
        return (_B)this;
    }

    default _B putAllToIntToStringMap(Map<? extends Integer, ? extends String> map) {
        intToStringMapBuilder().putAll(map);
        return (_B)this;
    }

    default _B putAllToIntToStringMap(Iterable<? extends Entry<? extends Integer, ? extends String>> entries) {
        intToStringMapBuilder().putAll(entries);
        return (_B)this;
    }

    default _B orderEntriesByValueToIntToStringMap(Comparator<? super String> valueComparator) {
        intToStringMapBuilder().orderEntriesByValue(valueComparator);
        return (_B)this;
    }

    _B intToStringBiMap(ImmutableBiMap<Integer, String> intToStringBiMap);

    ImmutableBiMap.Builder<Integer, String> intToStringBiMapBuilder();

    default _B putToIntToStringBiMap(Integer key, String value) {
        intToStringBiMapBuilder().put(key, value);
        return (_B)this;
    }

    default _B putToIntToStringBiMap(Map.Entry<? extends Integer, ? extends String> entry) {
        intToStringBiMapBuilder().put(entry);
        return (_B)this;
    }

    default _B putAllToIntToStringBiMap(Map<? extends Integer, ? extends String> map) {
        intToStringBiMapBuilder().putAll(map);
        return (_B)this;
    }

    default _B putAllToIntToStringBiMap(Iterable<? extends Entry<? extends Integer, ? extends String>> entries) {
        intToStringBiMapBuilder().putAll(entries);
        return (_B)this;
    }

    default _B orderEntriesByValueToIntToStringBiMap(Comparator<? super String> valueComparator) {
        intToStringBiMapBuilder().orderEntriesByValue(valueComparator);
        return (_B)this;
    }

}
