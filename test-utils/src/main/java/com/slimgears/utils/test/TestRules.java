package com.slimgears.utils.test;

import org.junit.rules.TestRule;

import java.util.Arrays;

public class TestRules {
    public final static TestRule empty = (base, description) -> base;

    public static TestRule combine(TestRule first, TestRule second) {
        return (base, description) -> second.apply(first.apply(base, description), description);
    }

    public static TestRule combine(TestRule... rules) {
        return Arrays.stream(rules).reduce(TestRules::combine).orElse(empty);
    }
}
