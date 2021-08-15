package com.slimgears.util.test.containers;

import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.function.Supplier;

public interface WaitPolicy {
    boolean waitForReady();

    static WaitPolicy delaySeconds(int seconds) {
        return () -> {
            try {
                Thread.sleep(seconds * 1000L);
            } catch (InterruptedException e) {
                return false;
            }
            return true;
        };
    }

    static WaitPolicy busyWaitSeconds(long timeoutSeconds, Supplier<Boolean> readinessSupplier) {
        return busyWaitMillis(timeoutSeconds*1000L, 100L, readinessSupplier);
    }

    static WaitPolicy busyWaitMillis(long timeoutMillis, long sleepTimeMillis, Supplier<Boolean> readinessSupplier) {
        return () -> {
            LocalDateTime expirationTime = LocalDateTime.now().plus(timeoutMillis, ChronoUnit.MILLIS);
            while (expirationTime.isAfter(LocalDateTime.now())) {
                if (readinessSupplier.get()) {
                    return true;
                }
                try {
                    Thread.sleep(sleepTimeMillis);
                } catch (InterruptedException e) {
                    return false;
                }
            }
            return false;
        };
    }
}
