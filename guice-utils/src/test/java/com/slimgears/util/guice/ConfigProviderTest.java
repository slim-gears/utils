/**
 *
 */
package com.slimgears.util.guice;

import org.junit.Assert;
import org.junit.Test;

import java.util.Properties;

public class ConfigProviderTest {
    @Test
    public void testMergeProperties() {
        Properties properties1 = ConfigProviders.create(p -> p.put("key1", "value1"));
        Properties properties2 = ConfigProviders.create(p -> p.put("key1", "${key1},value2"));
        Properties merged = ConfigProviders.create(
                ConfigProviders.mergeFrom(properties1),
                ConfigProviders.mergeFrom(properties2));
        Assert.assertTrue(merged.containsKey("key1"));
        Assert.assertEquals("value1,value2", merged.getProperty("key1"));
    }
}
