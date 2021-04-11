package com.slimgears.util.test.jupiter;

import com.google.common.base.Objects;
import com.slimgears.util.test.ExtensionRule;
import com.slimgears.util.test.ExtensionRules;
import org.junit.jupiter.api.extension.AfterEachCallback;
import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.Extension;
import org.junit.jupiter.api.extension.ExtensionContext;

import java.lang.reflect.Method;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class RuleExtension implements Extension, BeforeEachCallback, AfterEachCallback {
    private final ExtensionRule extensionRule = ExtensionRules.discover();
    private final Map<TestKey, AutoCloseable> autoCloseableMap = new HashMap<>();

    static class TestKey {
        private final Method method;
        private final Object target;

        TestKey(Method method, Object target) {
            this.method = method;
            this.target = target;
        }

        static TestKey of(ExtensionContext context) {
            return new TestKey(context.getRequiredTestMethod(), context.getRequiredTestInstance());
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(method, target);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof TestKey &&
                    Objects.equal(((TestKey) obj).method, method) &&
                    Objects.equal(((TestKey) obj).target, target);
        }
    }

    @Override
    public void beforeEach(ExtensionContext context) {
        autoCloseableMap.put(TestKey.of(context), extensionRule.prepare(context.getRequiredTestMethod(), context.getRequiredTestInstance()));
    }

    @Override
    public void afterEach(ExtensionContext context) {
        Optional.ofNullable(autoCloseableMap.remove(TestKey.of(context)))
                .ifPresent(autoCloseable -> {
                    try {
                        autoCloseable.close();
                    } catch (Exception e) {
                        throw new RuntimeException(e);
                    }
                });
    }
}
