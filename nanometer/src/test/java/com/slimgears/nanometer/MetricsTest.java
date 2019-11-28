package com.slimgears.nanometer;

import io.micrometer.core.instrument.Counter;
import io.micrometer.core.instrument.Gauge;
import io.micrometer.core.instrument.Tag;
import io.micrometer.core.instrument.Timer;
import io.micrometer.core.instrument.search.Search;
import io.micrometer.core.instrument.simple.SimpleMeterRegistry;
import io.reactivex.Observable;
import io.reactivex.Single;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;

import java.time.Duration;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;

@CollectMetrics(
        value = "test.metrics",
        level = MetricLevel.INFO,
        tags = {
                @CollectMetrics.Tag(key = "one", value = "1"),
                @CollectMetrics.Tag(key = "two", value = "2")
        })
public class MetricsTest {
    private final static double epsilon = 0.001;
    private final MetricCollector metricCollector = Metrics.collector(MetricsTest.class);

    @BeforeClass
    public static void setUpClass() {
        io.micrometer.core.instrument.Metrics.globalRegistry.add(new SimpleMeterRegistry());
    }

    @Before
    public void setUp() {
        io.micrometer.core.instrument.Metrics.globalRegistry.clear();
    }

    @Test
    public void testCounters() {
        Metrics.level(MetricLevel.INFO);
        metricCollector.tag("three", 3).counter("counter1").increment();
        metricCollector.tag("four", 4).counter("counter2").increment(2.0);

        assertCount(1.0, "counter1", Tag.of("three", "3"));
        assertCount(2.0, "counter2", Tag.of("four", "4"));
    }

    @Test
    public void testTimer() throws InterruptedException {
        Metrics.level(MetricLevel.INFO);
        MetricCollector.Timer.Stopper stopper1 = metricCollector
                .tag("three")
                .timer("timer1")
                .stopper()
                .start();

        Thread.sleep(10);
        stopper1.stop();

        MetricCollector.Timer.Stopper stopper2 = metricCollector
                .tag("four", 4)
                .timer("timer2")
                .stopper()
                .start();
        Thread.sleep(10);
        stopper2.stop();

        assertTimeGreaterThan(Duration.ofMillis(10), "timer1", Tag.of("three", ""));
    }

    @Test
    public void testGauge() {
        Metrics.level(MetricLevel.INFO);
        metricCollector
                .gauge("gauge1", MetricTag.of("three", "3"))
                .record(50);

        assertGauge(50, "gauge1", Tag.of("three", "3"));
    }

    @Test
    public void testMetricLevel() {
        Metrics.level(MetricLevel.INFO);

        metricCollector
                .level(MetricLevel.INFO)
                .tag("three", 3)
                .counter("counter1")
                .increment();
        metricCollector
                .level(MetricLevel.DEBUG)
                .tag("four", 4)
                .counter("counter2")
                .increment(2.0);

        Counter counter1 = io.micrometer.core.instrument.Metrics.globalRegistry
                .find("test.metrics.counter1")
                .tags(Arrays.asList(
                        Tag.of("one", "1"),
                        Tag.of("two", "2"),
                        Tag.of("three", "3")))
                .counter();
        Assert.assertNotNull(counter1);
        Assert.assertEquals(1.0, counter1.count(), 0.01);

        Counter counter2 = io.micrometer.core.instrument.Metrics.globalRegistry
                .find("test.metrics.counter2")
                .tags(Arrays.asList(
                        Tag.of("one", "1"),
                        Tag.of("two", "2"),
                        Tag.of("four", "4")))
                .counter();
        Assert.assertNull(counter2);
    }

    @Test
    public void testObservableTimer() {
        Metrics.level(MetricLevel.INFO);
        Metrics.level(MetricLevel.INFO);
        MetricCollector collector = Metrics.collector(getClass());
        AtomicBoolean throwException = new AtomicBoolean(false);

        Observable<Integer> observable = Observable.defer(
                () -> throwException.get()
                        ? Observable.<Integer>error(new RuntimeException("Test exception"))
                        : Observable.empty())
                .concatWith(Observable
                        .range(0, 10)
                        .concatMapSingle(i -> Single.just(i).delay(10, TimeUnit.MILLISECONDS)))
                .lift(collector.async()
                        .timeTillComplete("completeTime")
                        .timeTillFirst("firstElementTime")
                        .timeBetweenItems("timeBetweenItems")
                        .forObservable());

        observable.ignoreElements().blockingAwait();
        assertTimeGreaterThan(Duration.ofMillis(10), "firstElementTime");
        assertTimeGreaterThan(Duration.ofMillis(100), "completeTime");

        observable.ignoreElements().blockingAwait();
        assertTimeGreaterThan(Duration.ofMillis(10), "firstElementTime");
        assertTimeGreaterThan(Duration.ofMillis(100), "completeTime");
        assertTimeGreaterThan(Duration.ofMillis(10), "timeBetweenItems");
    }

    @Test
    public void testCountObservable() {
        Metrics.level(MetricLevel.INFO);
        MetricCollector collector = Metrics.collector(getClass());
        AtomicBoolean throwException = new AtomicBoolean(false);

        Observable<Integer> observable = Observable.defer(
                () -> throwException.get()
                        ? Observable.<Integer>error(new RuntimeException("Test exception"))
                        : Observable.empty())
                .concatWith(Observable
                .range(0, 10))
                .lift(collector.async()
                        .countItems("itemCounter", MetricTag.of("tag1", "value1"))
                        .countCompletions("completionCounter")
                        .countSubscriptions("subscriptionCounter")
                        .countErrors("errorCounter")
                        .forObservable());

        observable.subscribe();
        assertCount(1.0, "subscriptionCounter");
        assertCount(10.0, "itemCounter", Tag.of("tag1", "value1"));
        assertCount(1.0, "completionCounter");

        observable.subscribe();
        assertCount(2.0, "subscriptionCounter");
        assertCount(20.0, "itemCounter");
        assertCount(2.0, "completionCounter");

        throwException.set(true);
        observable.subscribe(val -> {}, e -> {});
        assertCount(3.0, "subscriptionCounter");
        assertCount(20.0, "itemCounter");
        assertCount(2.0, "completionCounter");
        assertCount(1.0, "errorCounter");
    }

    private Search search(String name, Tag... tags) {
        Search search = io.micrometer.core.instrument.Metrics.globalRegistry
                .find("test.metrics." + name)
                .tag("one", "1")
                .tag("two", "2");

        for (Tag tag : tags) {
            search = search.tag(tag.getKey(), tag.getValue());
        }
        return search;
    }

    @SuppressWarnings("SameParameterValue")
    private void assertTimeGreaterThan(Duration expectedMinDuration, String name, Tag... tags) {
        Timer timer = search(name, tags).timer();
        Assert.assertNotNull(timer);
        Assert.assertTrue(
                "Expected: greater than " + expectedMinDuration.toMillis() +
                        "ms, actual: " + timer.totalTime(TimeUnit.MILLISECONDS),
                timer.totalTime(TimeUnit.MILLISECONDS) >= expectedMinDuration.toMillis());
    }

    @SuppressWarnings("SameParameterValue")
    private void assertGauge(double expected, String name, Tag... tags) {
        Gauge gauge = search(name, tags).gauge();
        Assert.assertNotNull(gauge);
        Assert.assertEquals(expected, gauge.value(), epsilon);
    }

    private void assertCount(double expected, String name, Tag... tags) {
        Counter counter = search(name, tags).counter();
        Assert.assertNotNull(counter);
        Assert.assertEquals(expected, counter.count(), epsilon);
    }
}
