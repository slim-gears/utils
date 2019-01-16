package com.slimgears.utils.test;

import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import java.util.List;

@MyCustomRule(name = "testClass")
public class CustomRuleTest {
    @Rule public final MethodRule rule = MethodRules.annotationRule();

    @Test
    @MyCustomRule(name = "testMethod")
    public void testCustomRule() {
        List<String> names = MyCustomRule.Rule.names();
        Assert.assertNotNull(names);
        Assert.assertEquals(2, names.size());
        Assert.assertEquals("testClass", names.get(0));
        Assert.assertEquals("testMethod", names.get(1));
    }
}
