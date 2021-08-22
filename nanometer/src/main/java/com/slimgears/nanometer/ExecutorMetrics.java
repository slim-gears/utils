package com.slimgears.nanometer;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorMetrics {
    private final MetricCollector metricCollector;

    private ExecutorMetrics(MetricCollector metricCollector) {
        this.metricCollector = Optional.ofNullable(metricCollector).orElse(MetricCollector.empty()).name("task");
    }

    public static ExecutorMetrics create(MetricCollector collector) {
        return new ExecutorMetrics(collector);
    }

    public static Executor wrap(Executor executor, MetricCollector metricCollector) {
        AtomicInteger queueSize = new AtomicInteger();
        AtomicInteger currentlyRunningCount = new AtomicInteger();

        MetricCollector.Timer executionTimer = metricCollector.timer("executionDuration");
        MetricCollector.Timer pendingTimer = metricCollector.timer("pendingDuration");
        MetricCollector.Counter totalCompleteCount = metricCollector.counter("totalCompleteCount");
        MetricCollector.Counter totalQueuedCount = metricCollector.counter("totalQueuedCount");
        metricCollector.gauge("pendingCount").record(queueSize);
        metricCollector.gauge("runningCount").record(currentlyRunningCount);
        MetricCollector.Counter errorCount = metricCollector.counter("errorCount");
        return runnable -> {
            queueSize.incrementAndGet();
            MetricCollector.Timer.Stopper pendingStopper = pendingTimer.stopper().start();
            totalQueuedCount.increment();
            executor.execute(() -> {
                pendingStopper.stop();
                queueSize.decrementAndGet();
                currentlyRunningCount.incrementAndGet();
                try {
                    executionTimer.record(runnable);
                    totalCompleteCount.increment();
                } catch (Throwable e) {
                    errorCount.increment();
                } finally {
                    currentlyRunningCount.decrementAndGet();
                }
            });
        };
    }

    public Executor wrap(Executor executor, String name) {
        return wrap(executor, metricCollector.name(name));
    }
}
