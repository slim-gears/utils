package com.slimgears.sample;

import com.google.common.collect.ImmutableBiMap;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.Nullable;

@AutoValuePrototype
@AutoValueMetaData
public interface SampleGuavaValuePrototype {
    ImmutableList<Integer> intList();
    ImmutableSet<String> stringSet();
    ImmutableMap<Integer, String> intToStringMap();
    ImmutableBiMap<Integer, String> intToStringBiMap();
    @Nullable ImmutableList<String> optionalList();
}
