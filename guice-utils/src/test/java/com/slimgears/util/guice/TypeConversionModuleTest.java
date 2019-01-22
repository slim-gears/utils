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

    enum CustomEnum {
        Value1,
        Value2
    }

    @Inject @Named("test.class") CustomClass testValue;
    @Inject @Named("test.enum") CustomEnum enumValue;

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
                        .add("test.enum", "Value2")
                        .build());

        injector.injectMembers(this);
        Assert.assertNotNull(testValue);
        Assert.assertEquals("Hello, World!", testValue.value());
        Assert.assertEquals(CustomEnum.Value2, enumValue);
    }
}