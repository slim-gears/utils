/**
 *
 */
package com.slimgears.util.guice;

import com.google.inject.Guice;
import com.google.inject.Injector;
import com.slimgears.util.stream.Equality;
import org.junit.Assert;
import org.junit.Test;

import javax.inject.Inject;
import javax.inject.Named;
import java.util.Properties;
import java.util.Set;

public class PropertyInjectionTest {
    @Test
    public void testCustomTypeConversionModule() {
        Injector injector = Guice.createInjector(
                TypeConversionModule
                        .builder()
                        .isExactly(TypeConversionModuleTest.CustomClass.class).convert(TypeConversionModuleTest.CustomClass::new)
                        .build(),
                PropertyModules.builder()
                        .add("test.class", "Hello, World!")
                        .build());

        class Injectee {
            @Inject @Named("test.class") TypeConversionModuleTest.CustomClass testValue;
        }

        Injectee injectee = new Injectee();

        injector.injectMembers(injectee);
        Assert.assertNotNull(injectee.testValue);
        Assert.assertEquals("Hello, World!", injectee.testValue.value());
    }

    @Test
    public void testIntInjection() {
        Properties properties = ConfigProviders.create(
                p -> p.put("strValue", "Hello"),
                p -> p.put("intValue", "2"),
                p -> p.put("strArrayValue", "One, Two, Three"),
                p -> p.put("intArrayValue", "1, 2, 3 , 4"),
                p -> p.put("customArray", "val1, val2"),
                p -> p.put("customSet", "val3 , val4, val5"));

        class Injectee {
            @Inject @Named("strValue") String strVal;
            @Inject @Named("intValue") int intVal;
            @Inject @Named("strArrayValue") String[] strArrVal;
            @Inject @Named("intArrayValue") int[] intArrVal;
            @Inject @Named("customArray") CustomClass[] customArray;
            @Inject @Named("customSet") Set<CustomClass> customSet;
        }

        Injectee injectee = new Injectee();

        Injector injector = Guice.createInjector(
                new TypeConversionModule(),
                TypeConversionModule.builder().isExactly(CustomClass.class).convert(CustomClass::new).build(),
                PropertyModules.forProperties(properties));
        injector.injectMembers(injectee);

        Assert.assertEquals("Hello", injectee.strVal);
        Assert.assertEquals(2, injectee.intVal);
        Assert.assertEquals(3, injectee.strArrVal.length);
        Assert.assertEquals("Two", injectee.strArrVal[1]);
        Assert.assertEquals(4, injectee.intArrVal.length);
        Assert.assertEquals(3, injectee.intArrVal[2]);
        Assert.assertEquals(2, injectee.customArray.length);
        Assert.assertEquals("val1", injectee.customArray[0].value());
        Assert.assertEquals(3, injectee.customSet.size());
        Assert.assertTrue(injectee.customSet.contains(new CustomClass("val4")));
    }

    static class CustomClass {
        private final static Equality.Checker<CustomClass> equalityChecker = Equality
                .builder(CustomClass.class).add(o -> o.value).build();

        private final String value;

        public CustomClass(String value) {
            this.value = value;
        }

        public String value() {
            return this.value;
        }

        @Override
        public boolean equals(Object obj) {
            return equalityChecker.equals(this, obj);
        }

        @Override
        public int hashCode() {
            return equalityChecker.hashCode(this);
        }
    }
}
