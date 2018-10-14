package com.slimgears.sample;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

@AutoValuePrototype
public interface SampleGuavaValuePrototype {
    ImmutableList<Integer> intList();
    ImmutableSet<String> stringSet();
    ImmutableMap<Integer, String> intToStringMap();
    ImmutableBiMap<Integer, String> intToStringBiMap();
}
