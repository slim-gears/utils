package com.slimgears.util.test.containers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.UseAutoValueAnnotator;
import com.slimgears.util.autovalue.annotations.UseBuilderExtension;

import javax.annotation.Nullable;

@AutoValuePrototype
@UseBuilderExtension
@UseAutoValueAnnotator
interface ContainerConfigPrototype {
    String image();
    @Nullable String containerName();
    ImmutableList<String> command();
    ImmutableMap<String, String> environment();
    ImmutableMap<Integer, Integer> ports();
    ImmutableMap<String, String> volumes();
    @Nullable Integer delaySeconds();
}
