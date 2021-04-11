package com.slimgears.util.test;

import com.google.inject.Inject;
import com.slimgears.util.test.guice.UseModules;
import com.slimgears.util.test.jupiter.RuleExtension;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.inject.Named;

@ExtendWith(RuleExtension.class)
@UseModules(GuiceRuleTest.TestModule.class)
public class JupiterTest {
    @Inject @Named("test") private String injectedMagicString;

    @Test
    public void testJunit() {
        Assertions.assertEquals("magic string", injectedMagicString);
    }
}
