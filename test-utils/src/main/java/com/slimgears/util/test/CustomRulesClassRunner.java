package com.slimgears.util.test;

import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

import static com.slimgears.util.test.AnnotationRulesJUnit.rule;

public class CustomRulesClassRunner extends BlockJUnit4ClassRunner {
    public CustomRulesClassRunner(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected boolean isIgnored(FrameworkMethod child) {
        return super.isIgnored(child) || MethodRules.isIgnored(child);
    }

    @Override
    protected List<MethodRule> rules(Object target) {
        List<MethodRule> rules = new ArrayList<>(super.rules(target));
        rules.add(customRulesMethodRule());
        return rules;
    }

    protected MethodRule customRulesMethodRule() {
        return rule();
    }
}
