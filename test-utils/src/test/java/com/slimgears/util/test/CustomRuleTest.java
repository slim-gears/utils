package com.slimgears.util.test;

import com.slimgears.util.junit.MethodRules;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.MethodRule;

import java.util.List;

@MyCustomRule(name = "testClass")
public class CustomRuleTest {
    @Rule public final MethodRule rule = MethodRules.fromExtensionRule(ExtensionRules.annotationRule());

    @Test
    @MyCustomRule(name = "testMethod")
    public void testCustomRule() {
        List<String> names = MyCustomRule.RuleProvider.names();
        Assert.assertNotNull(names);
        Assert.assertEquals(2, names.size());
        Assert.assertEquals("testClass", names.get(0));
        Assert.assertEquals("testMethod", names.get(1));
    }
}
