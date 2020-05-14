package com.slimgears.nanometer;

import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.atomic.AtomicInteger;

public class ExecutorMetrics {
    private final MetricCollector metricCollector;

    private ExecutorMetrics(MetricCollector metricCollector) {
        this.metricCollector = metricCollector;
    }

    public static ExecutorMetrics create(MetricCollector collector) {
        return new ExecutorMetrics(collector);
    }

    public static Executor wrap(Executor executor, MetricCollector metricCollector) {
        AtomicInteger queueSize = new AtomicInteger();
        AtomicInteger currentlyRunningCount = new AtomicInteger();

        MetricCollector.Timer executionTimer = metricCollector.timer("task.executionDuration");
        MetricCollector.Timer pendingTimer = metricCollector.timer("task.pendingDuration");
        MetricCollector.Counter totalCompleteCount = metricCollector.counter("task.totalCompleteCount");
        MetricCollector.Counter totalQueuedCount = metricCollector.counter("task.totalQueuedCount");
        metricCollector.gauge("task.pendingCount").record(queueSize);
        metricCollector.gauge("task.runningCount").record(currentlyRunningCount);
        MetricCollector.Counter errorCount = metricCollector.counter("task.errorCount");
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
        MetricCollector metricCollector = Optional.ofNullable(this.metricCollector)
                .orElse(MetricCollector.empty())
                .name(name);
        return wrap(executor, metricCollector);
    }
}
