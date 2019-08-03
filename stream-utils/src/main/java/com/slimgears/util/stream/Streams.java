package com.slimgears.util.stream;

import java.util.*;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

@SuppressWarnings("WeakerAccess")
public class Streams {
    public static <T> Stream<Stream<T>> toBatch(Stream<T> source, int batchSize) {
        Spliterator<Spliterator<T>> batchSpliterator = batch(source.spliterator(), batchSize);
        return StreamSupport.stream(batchSpliterator, false)
                .map(spliterator -> StreamSupport.stream(spliterator, false));
    }

    public static <T, R extends T> Stream<R> ofType(Class<R> clazz, Stream<T> source) {
        return source.filter(clazz::isInstance).map(clazz::cast);
    }

    public static <T, R extends T> Function<T, Stream<R>> ofType(Class<R> clazz) {
        return item -> ofType(clazz, Stream.of(item));
    }

    public static <T> Function<T, T> self() {
        return s -> s;
    }

    public static <T> Stream<T> takeWhile(Stream<T> stream, Predicate<? super T> predicate) {
        return StreamSupport.stream(takeWhile(stream.spliterator(), predicate), false);
    }

    public static <T> Stream<T> fromIterator(Iterator<T> iterator) {
        return StreamSupport.stream(Spliterators.spliteratorUnknownSize(iterator, 0), false);
    }

    public static <T> Stream<T> fromIterable(Iterable<T> iterable) {
        return fromIterator(iterable.iterator());
    }

    public static <T> Stream<T> fromEnumeration(Enumeration<T> enumeration) {
        return StreamSupport.stream(
                new Spliterators.AbstractSpliterator<T>(Long.MAX_VALUE, Spliterator.ORDERED) {
                    public boolean tryAdvance(Consumer<? super T> action) {
                        if(enumeration.hasMoreElements()) {
                            action.accept(enumeration.nextElement());
                            return true;
                        }
                        return false;
                    }
                    public void forEachRemaining(Consumer<? super T> action) {
                        while(enumeration.hasMoreElements()) action.accept(enumeration.nextElement());
                    }
                }, false);
    }

    public static <T> Stream<T> orderByTopology(Stream<T> source, Function<T, Stream<T>> childrenGetter) {
        return orderByTopology(source, childrenGetter, new HashSet<>());
    }

    private static <T> Stream<T> orderByTopology(Stream<T> source, Function<T, Stream<T>> childrenGetter, Set<T> visited) {
        return source
                .filter(visited::add)
                .flatMap(val -> Stream.concat(orderByTopology(childrenGetter.apply(val), childrenGetter, visited), Stream.of(val)));
    }

    private static <T> Spliterator<T> takeWhile(Spliterator<T> spliterator, Predicate<? super T> predicate) {
        return new Spliterators.AbstractSpliterator<T>(spliterator.estimateSize(), 0) {
            private boolean finished = false;

            @Override
            public boolean tryAdvance(Consumer<? super T> action) {
                return spliterator.tryAdvance(item -> {
                    if (predicate.test(item)) {
                        action.accept(item);
                    } else {
                        finished = true;
                    }
                }) && !finished;
            }
        };
    }

    static class LimitedSpliterator<T> extends Spliterators.AbstractSpliterator<T> {
        private final Spliterator<T> source;
        private final AtomicInteger remaining;
        private final AtomicBoolean finished = new AtomicBoolean(false);

        LimitedSpliterator(Spliterator<T> source, int maxSize) {
            super(maxSize, 0);
            this.source = source;
            this.remaining = new AtomicInteger(maxSize);
        }

        @Override
        public boolean tryAdvance(Consumer<? super T> action) {
            if (remaining.decrementAndGet() == 0) {
                return false;
            }
            finished.set(source.tryAdvance(action));
            return finished.get();
        }
    }

    private static <T> Spliterator<Spliterator<T>> batch(Spliterator<T> source, int batchSize) {
        return new Spliterators.AbstractSpliterator<Spliterator<T>>(source.estimateSize() / batchSize, 0) {
            @Override
            public boolean tryAdvance(Consumer<? super Spliterator<T>> action) {
                LimitedSpliterator<T> spliterator = new LimitedSpliterator<>(source, batchSize);
                action.accept(spliterator);
                return !spliterator.finished.get();
            }
        };
    }
}
