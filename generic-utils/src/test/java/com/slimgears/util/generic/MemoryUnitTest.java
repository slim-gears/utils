package com.slimgears.util.generic;

import org.junit.Assert;
import org.junit.Test;

public class MemoryUnitTest {
    @Test
    public void testMemoryUnitConversion() {
        Assert.assertEquals(1 << 20, MemoryUnit.GIGABYTES.toKiloBytes(1));
    }
}
