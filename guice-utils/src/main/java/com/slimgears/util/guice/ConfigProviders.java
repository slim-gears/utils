/**
 */
package com.slimgears.util.guice;

import org.apache.commons.text.StringSubstitutor;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.stream.Stream;

public class ConfigProviders {
    public static final ConfigProvider empty = p -> {};

    public static ConfigProvider loadFrom(Callable<InputStream> streamSupplier) {
        return props -> {
            try (InputStream stream = streamSupplier.call()) {
                if (stream != null) {
                    props.load(stream);
                }
            } catch (Exception e) {
                throw new RuntimeException("Error occurred when loading properties", e);
            }
        };
    }

    public static ConfigProvider fromServiceLoader() {
        return props -> ServiceLoader.load(ConfigProvider.class, ConfigProviders.class.getClassLoader()).forEach(cp -> cp.apply(props));
    }

    public static ConfigProvider loadFromFile(Path path) {
        return loadFrom(() -> Files.newInputStream(path, StandardOpenOption.READ));
    }

    public static ConfigProvider loadFromFile(String path) {
        return loadFromFile(Paths.get(path));
    }

    public static ConfigProvider loadFromResource(String path) {
        return loadFrom(() -> ConfigProviders.class.getResourceAsStream(path));
    }

    public static ConfigProvider mergeFrom(Properties properties) {
        return props -> properties.stringPropertyNames()
                .forEach(name -> {
                    String value = properties.getProperty(name);
                    props.setProperty(name, StringSubstitutor.replace(value, props));
                });
    }

    public static ConfigProvider loadFromMap(Map<String, ?> values) {
        return props -> values.forEach((key, value) -> props.put(key, Optional.ofNullable(value).map(Object::toString).orElse("")));
    }

    public static ConfigProvider of(ConfigProvider... configs) {
        return props -> Stream.of(configs).forEach(c -> c.apply(props));
    }

    public static Properties create(ConfigProvider... configs) {
        Properties properties = new Properties();
        of(configs).apply(properties);
        return properties;
    }
}
