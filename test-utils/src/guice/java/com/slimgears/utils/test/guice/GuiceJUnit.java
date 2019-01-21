package com.slimgears.utils.test.guice;

import com.google.common.collect.ImmutableList;
import com.google.inject.Module;
import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.List;

public class GuiceJUnit extends BlockJUnit4ClassRunner {
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass
     * @throws InitializationError if the test class is malformed.
     */
    public GuiceJUnit(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<MethodRule> rules(Object target) {
        return ImmutableList.<MethodRule>builder()
                .addAll(super.rules(target))
                .add(rule())
                .build();
    }

    public static MethodRule rule(Module... modules) {
        return new GuiceMethodRule(modules);
    }
}
