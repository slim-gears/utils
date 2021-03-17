package com.slimgears.util.rx;

import io.reactivex.Completable;
import io.reactivex.ObservableSource;
import io.reactivex.Observer;
import io.reactivex.annotations.NonNull;
import io.reactivex.disposables.Disposable;
import io.reactivex.disposables.Disposables;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicReference;

public class BufferUntilIdleObservableSource<T> implements ObservableSource<List<T>> {
    private final ObservableSource<T> source;
    private final Duration maxIdleDuration;
    private final Duration maxBufferDuration;

    BufferUntilIdleObservableSource(ObservableSource<T> source, Duration maxIdleDuration, Duration maxBufferDuration) {
        this.source = source;
        this.maxIdleDuration = maxIdleDuration;
        this.maxBufferDuration = maxBufferDuration;
    }

    @Override
    public void subscribe(@NonNull Observer<? super List<T>> observer) {
        source.subscribe(new SourceObserver(observer));
    }

    class SourceObserver implements Observer<T> {
        private final Observer<? super List<T>> observer;
        private final AtomicReference<List<T>> currentBuffer = new AtomicReference<>(new ArrayList<>());
        private final AtomicReference<Disposable> idleTimerDisposable = new AtomicReference<>(Disposables.empty());
        private final AtomicReference<Disposable> bufferTimerDisposable = new AtomicReference<>(Disposables.empty());

        SourceObserver(Observer<? super List<T>> observer) {
            this.observer = observer;
        }

        @Override
        public void onSubscribe(Disposable d) {
            observer.onSubscribe(d);
        }

        @Override
        public void onNext(@NonNull T next) {
            synchronized (currentBuffer) {
                List<T> buf = currentBuffer.get();
                buf.add(next);
            }
            rescheduleFlush(idleTimerDisposable, maxIdleDuration);
        }

        @Override
        public void onError(Throwable e) {
            observer.onError(e);
        }

        @Override
        public void onComplete() {
            flush();
            cancelFlush(bufferTimerDisposable);
            observer.onComplete();
        }

        private void cancelFlush(AtomicReference<Disposable> disposable) {
            disposable.getAndSet(Disposables.empty()).dispose();
        }

        private void rescheduleFlush(AtomicReference<Disposable> disposable, Duration duration) {
            disposable
                    .getAndSet(Completable.timer(duration.toMillis(), TimeUnit.MILLISECONDS).subscribe(this::flush))
                    .dispose();
        }

        private synchronized void flush() {
            List<T> buffer;

            synchronized (currentBuffer) {
                buffer = currentBuffer.getAndSet(new ArrayList<>());
            }

            Optional
                    .of(buffer)
                    .filter(b -> !b.isEmpty())
                    .ifPresent(observer::onNext);
            cancelFlush(idleTimerDisposable);
            rescheduleFlush(bufferTimerDisposable, maxBufferDuration);
        }
    }
}
