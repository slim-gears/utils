/**
 */
package com.slimgears.util.guice;

import com.slimgears.util.stream.DoubleUtils;
import org.apache.commons.text.StringSubstitutor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.Map;
import java.util.Properties;
import java.util.ServiceLoader;
import java.util.concurrent.Callable;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class ConfigProviders {
    private final static Logger log = LoggerFactory.getLogger(ConfigProviders.class);
    public static final ConfigProvider empty = p -> {};

    public static ConfigProvider loadFrom(Callable<InputStream> streamSupplier) {
        return props -> {
            try (InputStream stream = streamSupplier.call()) {
                if (stream != null) {
                    props.load(stream);
                }
            } catch (Exception e) {
                log.debug("Could not load properties", e);
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
        return props -> setProperties(props, "", values);
    }

    public static ConfigProvider of(ConfigProvider... configs) {
        return props -> Stream.of(configs).forEach(c -> c.apply(props));
    }

    public static Properties create(ConfigProvider... configs) {
        Properties properties = new Properties();
        of(configs).apply(properties);
        return properties;
    }

    private static void setProperty(Properties properties, String key, Object value) {
        if (value instanceof Map) {
            //noinspection unchecked
            setProperties(properties, key + ".", (Map<String, Object>)value);
        } if (value instanceof List) {
            //noinspection unchecked
            properties.setProperty(key, ((List<Object>)value).stream().map(String::valueOf).collect(Collectors.joining(",")));
        } else if (value instanceof Double) {
            properties.setProperty(key, DoubleUtils.toString((double)value));
        } else {
            properties.setProperty(key, String.valueOf(value));
        }
    }

    private static void setProperties(Properties properties, String keyPrefix, Map<String, ?> values) {
        values.forEach((key, value) -> setProperty(properties,keyPrefix + key, value));
    }

}
