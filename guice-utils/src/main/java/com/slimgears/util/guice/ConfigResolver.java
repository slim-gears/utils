package com.slimgears.util.guice;

import com.slimgears.util.reflect.TypeToken;

public interface ConfigResolver {
    <T> T resolveProperty(String path, TypeToken<T> token);
}
