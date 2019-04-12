package com.slimgears.util.stream;

import java.util.Enumeration;
import java.util.Iterator;
import java.util.Spliterator;
import java.util.Spliterators;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

public class Streams {
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


    private static <T>Spliterator<T> takeWhile(Spliterator<T> spliterator, Predicate<? super T> predicate) {
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
}
