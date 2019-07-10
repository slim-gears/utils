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
    }

    @Test
    public void testConfigBinding() {
        Injector injector = Guice.createInjector(
                PropertyModules.forProperties(ConfigProviders.loadFromResource("/test-config.properties")),
                new TypeConversionModule(),
                new ConfigBindingModule());
        Config config = injector.getInstance(Config.class);
        assertThat(config, notNullValue());
        assertThat(config.name(), equalTo("Test name"));
        assertThat(config.number(), equalTo(101));
        assertThat(config.nestedConfig(), notNullValue());
        assertThat(config.nestedConfig().nestedName(), equalTo("Test nested name"));
        assertThat(config.stringArray(), equalTo(new String[] {"One", "Two", "Three"}));
        assertThat(config.intArray(), equalTo(new int[] {4, 5, 6, 7}));
    }
}
