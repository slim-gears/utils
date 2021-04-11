package com.slimgears.util.junit;

import com.slimgears.util.test.ExtensionRules;
import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class ExtensionRuleRunner extends BlockJUnit4ClassRunner {
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public ExtensionRuleRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<MethodRule> rules(Object target) {
        List<MethodRule> rules = super.rules(target);
        rules.add(MethodRules.fromExtensionRule(ExtensionRules.discover()));
        return rules;
    }
}
