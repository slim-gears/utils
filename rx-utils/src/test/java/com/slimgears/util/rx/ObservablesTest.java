package com.slimgears.util.rx;

import io.reactivex.Completable;
import io.reactivex.Observable;
import io.reactivex.internal.functions.Functions;
import org.junit.Ignore;
import org.junit.Test;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

public class ObservablesTest {
    @Test
    public void testBufferUntilIdle() throws InterruptedException {
        Observable.just(
                Observable.interval(10, TimeUnit.MILLISECONDS).take(30),
                Completable.timer(1000, TimeUnit.MILLISECONDS).andThen(Observable.interval(10, TimeUnit.MILLISECONDS).take(20)),
                Completable.timer(1000, TimeUnit.MILLISECONDS).andThen(Observable.interval(10, TimeUnit.MILLISECONDS).take(10)),
                Completable.timer(1000, TimeUnit.MILLISECONDS).andThen(Observable.interval(10, TimeUnit.MILLISECONDS).take(5)))
                .concatMap(Functions.identity())
                .compose(Observables.bufferUntilIdle(Duration.ofMillis(200)))
                .test()
                .await()
                .assertValueCount(4)
                .assertValueAt(0, l -> l.size() == 30)
                .assertValueAt(1, l -> l.size() == 20)
                .assertValueAt(2, l -> l.size() == 10)
                .assertValueAt(3, l -> l.size() == 5);
    }

    @Test
    public void testDoWhileWaiting() {
        Observable.interval(100, 100, TimeUnit.MILLISECONDS)
                .take(3)
                .compose(Observables.doWhileWaiting(Duration.ofMillis(50), duration -> System.out.println("Millis passed: " + duration.toMillis())))
                .doOnNext(n -> System.out.println("Next: " + n))
                .ignoreElements()
                .blockingAwait();
    }
}
