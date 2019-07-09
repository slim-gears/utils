package com.slimgears.util.test;

import com.google.inject.AbstractModule;
import com.slimgears.util.guice.ConfigProviders;
import com.slimgears.util.guice.PropertyModules;
import com.slimgears.util.test.guice.GuiceJUnit;
import com.slimgears.util.test.guice.UseModules;
import com.slimgears.util.test.guice.UseProperties;
import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

import javax.inject.Inject;
import javax.inject.Named;
import javax.inject.Provider;

@RunWith(GuiceJUnit.class)
@UseModules(UsePropertiesTest.Module.class)
@UseProperties({
        @UseProperties.Property(name = "test-value", value = "Test value from test class"),
        @UseProperties.Property(name = "test-int-value", value = "3000")
})
public class UsePropertiesTest {
    @Inject @Named("test-value") String testValue;
    @Inject @Named("test-value-not-overridden") String testNotOverridden;
    @Inject @Named("test-int-value") int testIntValue;
    @Inject Provider<TestInjectee> testInjecteeProvider;

    public static class Module extends AbstractModule {
        @Override
        protected void configure() {
            install(PropertyModules.forProperties(ConfigProviders.loadFromResource("/test-config.properties")));
        }
    }

    @Test
    public void testNonOverriddenValue() {
        Assert.assertEquals("Not overridden test value from configuration file", testNotOverridden);
    }

    @Test
    public void testUsePropertiesForClass() {
        Assert.assertEquals("Test value from test class", testValue);
        Assert.assertEquals(3000, testIntValue);
    }

    @Test
    @UseProperties({
            @UseProperties.Property(name = "test-value", value = "Test value from test method"),
            @UseProperties.Property(name = "test-int-value", value = "2000")
    })
    public void testUsePropertiesForTest() {
        Assert.assertEquals("Test value from test method", testValue);
        Assert.assertEquals(2000, testIntValue);
        Assert.assertEquals(2000, testInjecteeProvider.get().testIntValue);
    }

    static class TestInjectee {
        final int testIntValue;

        @Inject
        public TestInjectee(@Named("test-int-value") int testIntValue) {
            this.testIntValue = testIntValue;
        }
    }
}
