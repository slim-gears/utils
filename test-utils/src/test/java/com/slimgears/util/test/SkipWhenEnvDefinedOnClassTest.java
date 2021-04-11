package com.slimgears.util.test;

import com.slimgears.util.junit.CustomRulesClassRunner;
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
