package com.slimgears.utils.test;

import org.junit.rules.MethodRule;
import org.junit.runners.model.FrameworkMethod;
import org.junit.runners.model.Statement;

public interface AdditionalInfoMethodRule<T> {
    Statement apply(T info, Statement base, FrameworkMethod method, Object target);

    default MethodRule toMethodRule(T info) {
        return (base, method, target) -> AdditionalInfoMethodRule.this.apply(info, base, method, target);
    }
}
