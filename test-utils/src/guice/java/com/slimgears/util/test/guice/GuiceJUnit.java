package com.slimgears.util.test.guice;

import com.google.inject.Module;
import com.slimgears.util.test.CustomRulesClassRunner;
import org.junit.rules.MethodRule;
import org.junit.runners.model.InitializationError;

public class GuiceJUnit extends CustomRulesClassRunner {
    public GuiceJUnit(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected MethodRule customRulesMethodRule() {
        return rule();
    }

    @SuppressWarnings("WeakerAccess")
    public static MethodRule rule(Module... modules) {
        return new GuiceMethodRule(modules);
    }
}
