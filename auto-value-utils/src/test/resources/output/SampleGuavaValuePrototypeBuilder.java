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

@Generated("com.slimgears.util.autovalue.apt.extensions.BuilderExtension")
public interface SampleGuavaValuePrototypeBuilder<_B extends SampleGuavaValuePrototypeBuilder<_B>> {

    _B intList(ImmutableList<Integer> intList);
    
    ImmutableList.Builder<Integer> intListBuilder();
        
    default _B intListAdd(Integer element) {
        intListBuilder().add(element); 
        return (_B)this;
    }

    default _B intListAdd(Integer... elements) {
        intListBuilder().add(elements); 
        return (_B)this;
    }

    default _B intListAddAll(Iterable<? extends Integer> elements) {
        intListBuilder().addAll(elements); 
        return (_B)this;
    }

    default _B intListAddAll(Iterator<? extends Integer> elements) {
        intListBuilder().addAll(elements); 
        return (_B)this;
    }

    _B intToStringBiMap(ImmutableBiMap<Integer, String> intToStringBiMap);
    
    ImmutableBiMap.Builder<Integer, String> intToStringBiMapBuilder();
        
    default _B intToStringBiMapOrderEntriesByValue(Comparator<? super String> valueComparator) {
        intToStringBiMapBuilder().orderEntriesByValue(valueComparator); 
        return (_B)this;
    }

    default _B intToStringBiMapPut(Integer key, String value) {
        intToStringBiMapBuilder().put(key, value); 
        return (_B)this;
    }

    default _B intToStringBiMapPut(Map.Entry<? extends Integer, ? extends String> entry) {
        intToStringBiMapBuilder().put(entry); 
        return (_B)this;
    }

    default _B intToStringBiMapPutAll(Map<? extends Integer, ? extends String> map) {
        intToStringBiMapBuilder().putAll(map); 
        return (_B)this;
    }

    default _B intToStringBiMapPutAll(Iterable<? extends Entry<? extends Integer, ? extends String>> entries) {
        intToStringBiMapBuilder().putAll(entries); 
        return (_B)this;
    }

    _B intToStringMap(ImmutableMap<Integer, String> intToStringMap);
    
    ImmutableMap.Builder<Integer, String> intToStringMapBuilder();
        
    default _B intToStringMapOrderEntriesByValue(Comparator<? super String> valueComparator) {
        intToStringMapBuilder().orderEntriesByValue(valueComparator); 
        return (_B)this;
    }

    default _B intToStringMapPut(Integer key, String value) {
        intToStringMapBuilder().put(key, value); 
        return (_B)this;
    }

    default _B intToStringMapPut(Map.Entry<? extends Integer, ? extends String> entry) {
        intToStringMapBuilder().put(entry); 
        return (_B)this;
    }

    default _B intToStringMapPutAll(Map<? extends Integer, ? extends String> map) {
        intToStringMapBuilder().putAll(map); 
        return (_B)this;
    }

    default _B intToStringMapPutAll(Iterable<? extends Entry<? extends Integer, ? extends String>> entries) {
        intToStringMapBuilder().putAll(entries); 
        return (_B)this;
    }

    _B optionalList(ImmutableList<String> optionalList);

    _B stringSet(ImmutableSet<String> stringSet);
    
    ImmutableSet.Builder<String> stringSetBuilder();
        
    default _B stringSetAdd(String element) {
        stringSetBuilder().add(element); 
        return (_B)this;
    }

    default _B stringSetAdd(String... elements) {
        stringSetBuilder().add(elements); 
        return (_B)this;
    }

    default _B stringSetAddAll(Iterable<? extends String> elements) {
        stringSetBuilder().addAll(elements); 
        return (_B)this;
    }

    default _B stringSetAddAll(Iterator<? extends String> elements) {
        stringSetBuilder().addAll(elements); 
        return (_B)this;
    }

}
