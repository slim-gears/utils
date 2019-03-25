package com.slimgears.util.test;

import org.junit.rules.MethodRule;
import org.junit.runners.BlockJUnit4ClassRunner;
import org.junit.runners.model.InitializationError;

import java.util.ArrayList;
import java.util.List;

public class AnnotationRulesJUnit extends BlockJUnit4ClassRunner{
    /**
     * Creates a BlockJUnit4ClassRunner to run {@code klass}
     *
     * @param klass Test class
     * @throws InitializationError if the test class is malformed.
     */
    public AnnotationRulesJUnit(Class<?> klass) throws InitializationError {
        super(klass);
    }

    @Override
    protected List<MethodRule> rules(Object target) {
        ArrayList<MethodRule> rules = new ArrayList<>(super.rules(target));
        rules.add(rule());
        return rules;
    }

    public static MethodRule rule() {
        return MethodRules.annotationRule();
    }
}
