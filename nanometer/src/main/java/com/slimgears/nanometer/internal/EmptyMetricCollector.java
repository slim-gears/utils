package com.slimgears.nanometer.internal;

import com.slimgears.nanometer.MetricCollector;
import com.slimgears.nanometer.MetricFilter;
import com.slimgears.nanometer.MetricLevel;
import com.slimgears.nanometer.MetricTag;

import java.util.function.ToDoubleFunction;

public class EmptyMetricCollector extends AbstractMetricCollector {
    public static final MetricCollector instance = new EmptyMetricCollector();
    public static final MetricCollector.Factory factory = new MetricCollector.Factory() {
        @Override
        public MetricCollector create() {
            return instance;
        }

        @Override
        public MetricCollector.Factory filter(MetricFilter filter) {
            return this;
        }
    };

    private final static Counter emptyCounter = value -> {};

    final static Timer.Stopper emptyStopper = new Timer.Stopper() {
        @Override
        public Timer.Stopper start() {
            return this;
        }

        @Override
        public Timer.Stopper stop() {
            return this;
        }
    };

    private final static Timer emptyTimer = duration -> {};

    private final static Gauge emptyGauge = new Gauge() {
        @Override
        public <T> void record(T object, ToDoubleFunction<T> valueProducer) {
        }

        @Override
        public <N extends Number> void record(N number) {
        }
    };

    public EmptyMetricCollector() {
    }

    @Override
    public Counter counter(String name, MetricTag... tags) {
        return emptyCounter;
    }

    @Override
    public Timer timer(String name, MetricTag... tags) {
        return emptyTimer;
    }

    @Override
    public Gauge gauge(String name, MetricTag... tags) {
        return emptyGauge;
    }
}
