/**
 *
 */
package com.slimgears.apt.util;

import java.util.*;
import java.util.function.*;
import java.util.stream.Stream;

public class VisitorUtils {
    public static <T, E extends T, B extends AbstractBuilder<T, ?, Void, Void, B>> List<E> findAllOf(Class<E> cls, T element, Supplier<? extends B> builderSupplier) {
        List<E> elements = new ArrayList<>();
        //noinspection unchecked
        builderSupplier.get()
                .onAny((T el) -> {
                    if (cls.isInstance(el)) {
                        elements.add(cls.cast(el));
                    }
                })
                .visit(element, null);
        return elements;
    }

    public static <E, P, R> BiFunction<E, P, R> doNothing() {
        return (e, p) -> null;
    }

    public static <T> BinaryOperator<T> takeFirstNonNull() {
        return (a, b) -> Optional.ofNullable(a).orElse(b);
    }

    public static <T> BinaryOperator<T> takeLastNonNull() {
        return (a, b) -> Optional.ofNullable(b).orElse(a);
    }

    public static <T> BinaryOperator<T> takeFirst() {
        return (a, b) -> a;
    }

    public static <T> BinaryOperator<T> takeFirstOrDefault(T defaultVal) {
        return (a, b) -> Optional.ofNullable(a).orElse(defaultVal);
    }

    public static <T> BinaryOperator<T> takeLast() {
        return (a, b) -> b;
    }

    public static <T> BinaryOperator<T> takeLastOrDefault(T defaultVal) {
        return (a, b) -> Optional.ofNullable(b).orElse(defaultVal);
    }

    static abstract class AbstractBuilder<T, V, R, P, B extends AbstractBuilder<T, V, R, P, B>> {
        private final BinaryOperator<R> resultCombiner;
        protected BiPredicate<T, P> filter = (e, p) -> true;
        protected BiFunction<T, P, R> onAny = doNothing();
        protected Map<Class<? extends T>, BiFunction<? extends T, P, R>> listeners = new HashMap<>();

        protected AbstractBuilder(BinaryOperator<R> resultCombiner) {
            this.resultCombiner = resultCombiner;
        }

        protected abstract B self();

        public B filter(BiPredicate<T, P> predicate) {
            this.filter = this.filter.and(predicate);
            return self();
        }

        public <E extends T> B on(Class<E> elementClass, BiFunction<E, P, R> listener) {
            //noinspection unchecked
            BiFunction<E, P, R> newListener = Optional
                    .ofNullable(listeners.get(elementClass))
                    .map(l -> combine((BiFunction<E, P, R>) l, listener))
                    .orElse(listener);
            listeners.put(elementClass, newListener);
            return self();
        }

        public <E extends T> B on(Class<E> elementClass, Function<E, R> consumer) {
            return on(elementClass, ignoreParam(consumer));
        }

        public <E extends T> B on(Class<E> elementClass, Consumer<E> consumer) {
            return on(elementClass, ignoreParam(consumer));
        }

        public B onAny(BiFunction<T, P, R> consumer) {
            this.onAny = combine(this.onAny, consumer);
            return self();
        }

        public B onAny(Function<T, R> consumer) {
            return onAny(ignoreParam(consumer));
        }

        public B onAny(Consumer<T> consumer) {
            return onAny(ignoreParam(consumer));
        }

        protected <E> BiFunction<E, P, R> combine(BiFunction<E, P, R> a, BiFunction<E, P, R> b) {
            return (e, p) -> resultCombiner.apply(a.apply(e, p), b.apply(e, p));
        }

        protected  <E> BiFunction<E, P, R> ignoreParam(Function<E, R> func) {
            return (e, p) -> func.apply(e);
        }

        protected  <E> BiFunction<E, P, R> ignoreParam(Consumer<E> consumer) {
            return ignoreParam(e -> {
                consumer.accept(e);
                return null;
            });
        }

        @SafeVarargs
        protected final R aggregate(R... results) {
            return Stream.of(results)
                    .filter(Objects::nonNull)
                    .reduce(resultCombiner)
                    .orElse(null);
        }

        protected final R aggregate(R first, Stream<R> others) {
            return Stream.concat(Stream.of(first), others)
                    .filter(Objects::nonNull)
                    .reduce(resultCombiner)
                    .orElse(null);
        }

        protected <E extends T> BiFunction<E, P, R> listenerOf(Class<E> cls) {
            //noinspection unchecked
            return Optional
                    .ofNullable(listeners.get(cls))
                    .map(l -> (BiFunction<E, P, R>)l)
                    .orElseGet(VisitorUtils::doNothing);
        }

        public abstract V build();
        protected abstract R accept(T element, V visitor, P param);

        public void visit(T element, P param) {
            accept(element, build(), param);
        }
    }
}
