package com.slimgears.util.stream;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Objects;
import java.util.function.BiFunction;
import java.util.function.Function;

public class Equality {
    public interface Checker<T> {
        boolean equals(T self, Object other);
        int hashCode(T self);
    }

    public static <T> Checker<T> of(BiFunction<T, Object, Boolean> equalityTester, Function<T, Integer> hashCodeGen) {
        return new Checker<T>() {
            public boolean equals(T self, Object other) {
                return equalityTester.apply(self, other);
            }

            @Override
            public int hashCode(T self) {
                return hashCodeGen.apply(self);
            }
        };
    }

    public static <T> Checker<T> of(Class<T> cls, Function<T, Object> field) {
        Checker<T> checker = ofField(cls, field);
        return of(checkNullAndIdentity(checker::equals), checker::hashCode);
    }

    private static <T> BiFunction<T, Object, Boolean> checkNullAndIdentity(BiFunction<T, Object, Boolean> equalityTester) {
        return (self, other) -> (other == self) || (other != null) && equalityTester.apply(self, other);
    }

    @SuppressWarnings("unchecked")
    private static <T> Checker<T> ofField(Class<T> cls, Function<T, Object> field) {
        BiFunction<T, Object, Boolean> equalityTester = (self, other) ->
                cls.isInstance(other) &&
                Objects.equals(field.apply(self), field.apply((T)other));
        Function<T, Integer> hashCodeGen = self -> Objects.hash(field.apply(self));
        return of(equalityTester, hashCodeGen);
    }

    public static <T> Builder<T> builder(Class<T> cls) {
        return new Builder<>(cls);
    }

    public static class Builder<T> {
        private final Collection<Function<T, Object>> fields = new ArrayList<>();
        private final Class<T> cls;

        public Builder(Class<T> cls) {
            this.cls = cls;
        }

        public Builder<T> add(Function<T, Object> field) {
            this.fields.add(field);
            return this;
        }

        @SuppressWarnings("unchecked")
        public Checker<T> build() {
            BiFunction<T, Object, Boolean> equalityChecker = (self, other) -> (other == self) ||
                    (cls.isInstance(other) && fields.stream()
                            .allMatch(f -> Objects.equals(f.apply(self), f.apply((T)other))));
            Function<T, Integer> hashCodeGen = self -> Objects.hash(fields.stream().map(f -> f.apply(self)).toArray(Object[]::new));
            return of(equalityChecker, hashCodeGen);
        }
    }
}
