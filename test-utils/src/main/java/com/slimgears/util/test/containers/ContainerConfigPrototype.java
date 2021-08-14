package com.slimgears.util.test.containers;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.UseAutoValueAnnotator;
import com.slimgears.util.autovalue.annotations.UseBuilderExtension;
import lombok.SneakyThrows;

import javax.annotation.Nullable;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.time.temporal.Temporal;
import java.time.temporal.TemporalUnit;
import java.util.concurrent.TimeUnit;
import java.util.function.Supplier;

@AutoValuePrototype
@UseBuilderExtension
@UseAutoValueAnnotator
interface ContainerConfigPrototype {
    interface ReadinessWaitPolicy {
        boolean waitForReady();

        static ReadinessWaitPolicy delay(int seconds) {
            return () -> {
                try {
                    Thread.sleep(seconds * 1000L);
                } catch (InterruptedException e) {
                    return false;
                }
                return true;
            };
        }

        static ReadinessWaitPolicy busyWaitSeconds(long timeoutSeconds, Supplier<Boolean> readinessSupplier) {
            return busyWait(timeoutSeconds*1000L, 100L, readinessSupplier);
        }

        static ReadinessWaitPolicy busyWait(long timeoutMillis, long sleepTimeMillis, Supplier<Boolean> poller) {
            return () -> {
                LocalDateTime expirationTime = LocalDateTime.now().plus(timeoutMillis, ChronoUnit.MILLIS);
                while (expirationTime.isAfter(LocalDateTime.now())) {
                    if (poller.get()) {
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

    String image();
    @Nullable String containerName();
    ImmutableList<String> command();
    ImmutableMap<String, String> environment();
    ImmutableMap<Integer, Integer> ports();
    ImmutableMap<String, String> volumes();
    @Nullable ReadinessWaitPolicy waitPolicy();
}
