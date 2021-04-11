package com.slimgears.util.test;

import com.slimgears.util.junit.CustomRulesClassRunner;
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
