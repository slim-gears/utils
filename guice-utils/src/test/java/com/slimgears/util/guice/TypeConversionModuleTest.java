package com.slimgears.util.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;

public class TypeConversionModuleTest {
    static class CustomClass {
        private final String value;

        public CustomClass(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }
    }

    @Inject @Named("test.class") CustomClass testValue;

    @Test
    public void testCustomTypeConversionModule() {
        Injector injector = Guice.createInjector(
                new TypeConversionModule(),
                TypeConversionModule
                        .builder()
                        .isExactly(CustomClass.class).convert(CustomClass::new)
                        .build(),
                PropertyModules.builder()
                        .add("test.class", "Hello, World!")
                        .build());
        injector.injectMembers(this);
        Assert.assertNotNull(testValue);
        Assert.assertEquals("Hello, World!", testValue.value());
    }
}