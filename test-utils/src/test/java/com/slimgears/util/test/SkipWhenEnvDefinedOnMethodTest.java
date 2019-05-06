package com.slimgears.util.test;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(CustomRulesClassRunner.class)
public class SkipWhenEnvDefinedOnMethodTest {
    @Test @SkipWhenPathDefined
    public void testShouldBeIgnored() {
        Assert.fail();
    }
}
