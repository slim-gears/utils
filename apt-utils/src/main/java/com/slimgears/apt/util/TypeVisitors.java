/**
 *
 */
package com.slimgears.apt.util;

import javax.lang.model.type.*;
import javax.lang.model.util.AbstractTypeVisitor8;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;
import java.util.stream.Stream;

import static com.slimgears.apt.util.VisitorUtils.takeFirstNonNull;

public class TypeVisitors {
    public static <R, P> Builder<R, P> builder() {
        return builder(takeFirstNonNull());
    }

    public static <R, P> Builder<R, P> builder(BinaryOperator<R> resultCombiner) {
        return new Builder<>(resultCombiner);
    }

    public static <E extends TypeMirror> List<E> findAllOf(Class<E> cls, TypeMirror e) {
        return VisitorUtils.findAllOf(cls, e, TypeVisitors::builder);
    }

    public static class Builder<R, P> extends VisitorUtils.AbstractBuilder<TypeMirror, TypeVisitor<R, P>, R, P, Builder<R, P>> {
        public Builder(BinaryOperator<R> resultCombiner) {
            super(resultCombiner);
        }

        @Override
        protected Builder<R, P> self() {
            return this;
        }

        @Override
        protected R accept(TypeMirror element, TypeVisitor<R, P> visitor, P param) {
            return element.accept(visitor, param);
        }

        public TypeVisitor<R, P> build() {
            return new AbstractTypeVisitor8<R, P>() {
                private Map<TypeMirror, R> visitedElements = new HashMap<>();

                @Override
                public R visitPrimitive(PrimitiveType t, P p) {
                    return visitElement(PrimitiveType.class, t, p);
                }

                @Override
                public R visitNull(NullType t, P p) {
                    return visitElement(NullType.class, t, p);
                }

                @Override
                public R visitArray(ArrayType t, P p) {
                    return visitElement(ArrayType.class, t, p, t.getComponentType());
                }

                @Override
                public R visitDeclared(DeclaredType t, P p) {
                    return visitElement(DeclaredType.class, t, p, t.getTypeArguments());
                }

                @Override
                public R visitError(ErrorType t, P p) {
                    return visitElement(ErrorType.class, t, p, t.getTypeArguments());
                }

                @Override
                public R visitTypeVariable(TypeVariable t, P p) {
                    return visitElement(TypeVariable.class, t, p, t.getLowerBound(), t.getUpperBound());
                }

                @Override
                public R visitWildcard(WildcardType t, P p) {
                    return visitElement(WildcardType.class, t, p, t.getSuperBound(), t.getSuperBound());
                }

                @Override
                public R visitExecutable(ExecutableType t, P p) {
                    return visitElement(ExecutableType.class, t, p,
                            Collections.singletonList(t.getReturnType()),
                            t.getParameterTypes(),
                            t.getTypeVariables());
                }

                @Override
                public R visitNoType(NoType t, P p) {
                    return visitElement(NoType.class, t, p);
                }

                @Override
                public R visitUnion(UnionType t, P p) {
                    return visitElement(UnionType.class, t, p, t.getAlternatives());
                }

                @Override
                public R visitIntersection(IntersectionType t, P p) {
                    return visitElement(IntersectionType.class, t, p, t.getBounds());
                }

                private <E extends TypeMirror> R visitElement(Class<E> cls, E element, P param, TypeMirror childItem, TypeMirror... childItems) {
                    return visitElement(cls, element, param, Collections.singletonList(childItem), Arrays.asList(childItems));
                }

                @SafeVarargs
                private final <E extends TypeMirror> R visitElement(Class<E> cls, E element, P param, List<? extends TypeMirror>... childItems) {
                    if (!filter.test(element, param)) {
                        return null;
                    }

                    BiFunction<E, P, R> func = listenerOf(cls);

                    return visitedElements.computeIfAbsent(
                            element,
                            _element -> aggregate(
                                    aggregate(onAny.apply(element, param), func.apply(element, param)),
                                    Stream.of(childItems).flatMap(Collection::stream).map(el -> el.accept(this, param))));

                }
            };
        }
    }
}
