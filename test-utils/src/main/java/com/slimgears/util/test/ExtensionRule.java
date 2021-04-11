package com.slimgears.util.test;

import java.lang.reflect.Method;

public interface ExtensionRule {
    AutoCloseable prepare(Method method, Object target);

    static ExtensionRule empty() {
        return (method, target) -> () -> {};
    }

    default ExtensionRule andThen(ExtensionRule other) {
        ExtensionRule self = this;
        return (method, target) -> {
            AutoCloseable first = self.prepare(method, target);
            AutoCloseable second = other.prepare(method, target);
            return () -> {
                second.close();
                first.close();
            };
        };
    }
}
