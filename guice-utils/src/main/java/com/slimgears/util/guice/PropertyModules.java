/**
 */
package com.slimgears.util.guice;

import com.google.common.collect.ImmutableMap;
import com.google.inject.AbstractModule;
import com.google.inject.Module;
import com.google.inject.name.Names;

import java.util.Map;
import java.util.Properties;

public class PropertyModules {
    public static Module forProperties(ConfigProvider... loaders) {
        return forProperties(ConfigProviders.create(loaders));
    }

    public static Module forProperties(Properties properties) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                Names.bindProperties(binder(), properties);
            }
        };
    }

    public static Module forMap(Map<String, String> values) {
        return new AbstractModule() {
            @Override
            protected void configure() {
                Names.bindProperties(binder(), values);
            }
        };
    }

    public static Builder builder() {
        return new Builder();
    }

    public static class Builder {
        private final ImmutableMap.Builder<String, String> mapBuilder = ImmutableMap.builder();

        public Builder add(String key, String value) {
            mapBuilder.put(key, value);
            return this;
        }

        public Module build() {
            return forMap(mapBuilder.build());
        }
    }
}
