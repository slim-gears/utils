package com.slimgears.util.junit;

import lombok.SneakyThrows;
import org.junit.rules.ExternalResource;
import org.junit.rules.TestRule;

import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Supplier;

public class ResourceRules {
    public static TestRule toRule(Supplier<AutoCloseable> autoCloseableSupplier) {
        AtomicReference<AutoCloseable> closeable = new AtomicReference<>(() -> {});
        return new ExternalResource() {
            @Override
            protected void before() {
                closeable.set(autoCloseableSupplier.get());
            }

            @Override
            @SneakyThrows
            protected void after() {
                if (System.getProperty("dockerRules.keepContainer") == null) {
                    closeable.get().close();
                }
            }
        };
    }
}
