package com.slimgears.util.stream;

import org.junit.Assert;
import org.junit.Test;

public class EqualityTest {
    static class CustomClass {
        private final static Equality.Checker<CustomClass> CHECKER = Equality
                .builder(CustomClass.class)
                .add(o -> o.strVal)
                .add(o -> o.intVal)
                .build();

        public int intVal;
        public String strVal;

        CustomClass(int intVal, String strVal) {
            this.intVal = intVal;
            this.strVal = strVal;
        }

        @Override
        public boolean equals(Object other) {
            return CHECKER.equals(this, other);
        }

        @Override
        public int hashCode() {
            return CHECKER.hashCode(this);
        }
    }

    @Test
    public void testEqualityBuilder() {
        CustomClass instance1 = new CustomClass(1, "1");
        CustomClass instance2 = new CustomClass(2, "1");
        CustomClass instance3 = new CustomClass(1, "2");
        CustomClass instance1_1 = new CustomClass(1, "1");

        Assert.assertEquals(instance1, instance1_1);

        Assert.assertEquals(instance1_1, instance1);
        Assert.assertEquals(instance1.hashCode(), instance1_1.hashCode());

        Assert.assertNotEquals(instance1.hashCode(), instance2.hashCode());
        Assert.assertNotEquals(instance1, instance2);

        Assert.assertNotEquals(instance1, instance3);
        Assert.assertNotEquals(instance1.hashCode(), instance3.hashCode());
    }
}