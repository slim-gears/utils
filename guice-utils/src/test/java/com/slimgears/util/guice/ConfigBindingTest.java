package com.slimgears.util.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.*;
import static org.hamcrest.MatcherAssert.assertThat;

public class ConfigBindingTest {
    interface NestedConfig {
        String nestedName();
    }

    @ConfigBinding("test.node.config")
    interface Config {
        String name();
        int number();
        String[] stringArray();
        int[] intArray();
        NestedConfig nestedConfig();
        NestedConfig[] nestedConfigArray();
    }

    @Test
    public void testConfigBinding() {
        Injector injector = Guice.createInjector(
                PropertyModules.forProperties(ConfigProviders.loadFromResource("/test-config.properties")),
                new TypeConversionModule(),
                ConfigBindingModule.create());
        Config config = injector.getInstance(Config.class);
        assertThat(config.toString(), equalTo("Configuration(path: test.node.config)"));
        assertThat(config, notNullValue());
        assertThat(config.name(), equalTo("Test name"));
        assertThat(config.number(), equalTo(101));
        assertThat(config.nestedConfig(), notNullValue());
        assertThat(config.nestedConfig().nestedName(), equalTo("Test nested name"));
        assertThat(config.stringArray(), equalTo(new String[] {"One", "Two", "Three"}));
        assertThat(config.intArray(), equalTo(new int[] {4, 5, 6, 7}));
        assertThat(config.nestedConfigArray(), notNullValue());
        assertThat(config.nestedConfigArray().length, equalTo(3));
        assertThat(config.nestedConfigArray()[1].nestedName(), equalTo("Test nested name 1"));
    }
}
