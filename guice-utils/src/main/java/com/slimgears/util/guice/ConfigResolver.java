package com.slimgears.util.guice;

import com.google.common.reflect.TypeToken;

@SuppressWarnings("UnstableApiUsage")
public interface ConfigResolver {
    <T> T resolveProperty(String path, TypeToken<T> token);
}
