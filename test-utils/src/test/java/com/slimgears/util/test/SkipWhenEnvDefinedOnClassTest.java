package com.slimgears.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CustomRulesClassRunner.class)
@SkipWhenPathDefined
public class SkipWhenEnvDefinedOnClassTest {
    @Test
    public void testShouldBeIgnored() {
        Assert.fail();
    }
}
