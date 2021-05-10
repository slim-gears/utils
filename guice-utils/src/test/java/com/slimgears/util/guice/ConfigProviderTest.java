/**
 *
 */
package com.slimgears.util.guice;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.contrib.java.lang.system.EnvironmentVariables;

import java.util.Properties;

public class ConfigProviderTest {
    @Rule
    public final EnvironmentVariables env = new EnvironmentVariables();

    @Test
    public void testMergeProperties() {
        Properties properties1 = ConfigProviders.create(p -> p.put("key1", "value1"));
        Properties properties2 = ConfigProviders.create(
                p -> p.put("key1", "${key1},value2"),
                p -> p.put("key2", "${key3}"));
        Properties merged = ConfigProviders.create(
                ConfigProviders.mergeFrom(properties1),
                ConfigProviders.mergeFrom(properties2));
        Assert.assertTrue(merged.containsKey("key1"));
        Assert.assertEquals("value1,value2", merged.getProperty("key1"));
        Assert.assertEquals("${key3}", merged.getProperty("key2"));
    }

    @Test
    public void testEvaluator() {
        env.set("TEST_KEY_1", "test_value");
        Properties properties1 = ConfigProviders.create(
                p -> p.put("key1", "value-${TEST_KEY_1}"),
                ConfigProviders.evaluator());
        Assert.assertEquals("value-test_value", properties1.getProperty("key1"));
    }
}
