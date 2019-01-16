package com.slimgears.utils.test.guice;

import com.google.inject.Module;
import org.junit.rules.MethodRule;

public class GuiceJUnit {
    public static MethodRule rule(Module... modules) {
        return new GuiceMethodRule(modules);
    }
}
