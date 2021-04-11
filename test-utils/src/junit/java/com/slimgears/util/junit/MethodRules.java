package com.slimgears.util.junit;

import com.slimgears.util.test.ExtensionRule;
import org.junit.rules.MethodRule;
import org.junit.runners.model.Statement;

public class MethodRules {
    public static MethodRule fromExtensionRule(ExtensionRule rule) {
        return (base, method, target) -> new Statement() {
            @Override
            public void evaluate() throws Throwable {
                AutoCloseable closeable = rule.prepare(method.getMethod(), target);
                try {
                    base.evaluate();
                } finally {
                    closeable.close();
                }
            }
        };
    }
}
