package com.slimgears.apt.util;

import javax.lang.model.element.*;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.util.AbstractElementVisitor8;
import java.util.*;
import java.util.function.BiFunction;
import java.util.function.BinaryOperator;

import static com.slimgears.apt.util.VisitorUtils.takeFirstNonNull;

public class ElementVisitors {
    public static <P, R> Builder<R, P> builder() {
        return builder(takeFirstNonNull());
    }

    public static <P, R> Builder<R, P> builder(BinaryOperator<R> resultCombiner) {
        return new Builder<>(resultCombiner);
    }

    public static Collection<DeclaredType> allReferencedTypes(Element e) {
        List<DeclaredType> types = new ArrayList<>();
        e.accept(ElementVisitors.<Void, Void>builder()
                .on(TypeElement.class, type -> { types.addAll(TypeVisitors.findAllOf(DeclaredType.class, type.asType())); })
                .on(VariableElement.class, var -> { types.addAll(TypeVisitors.findAllOf(DeclaredType.class, var.asType())); })
                .on(ExecutableElement.class, ex -> { types.addAll(TypeVisitors.findAllOf(DeclaredType.class, ex.getReturnType())); })
                .build(), null);
        return new HashSet<>(types);
    }

    public static <E extends Element> List<E> findAllOf(Class<E> cls, Element e) {
        return VisitorUtils.findAllOf(cls, e, ElementVisitors::builder);
    }

    public static class Builder<R, P> extends VisitorUtils.AbstractBuilder<Element, ElementVisitor<R, P>, R, P, Builder<R, P>> {
        public Builder(BinaryOperator<R> combiner) {
            super(combiner);
        }

        @Override
        protected Builder<R, P> self() {
            return this;
        }

        @Override
        public ElementVisitor<R, P> build() {
            return new AbstractElementVisitor8<R, P>() {
                private Map<Element, R> visitedElements = new HashMap<>();

                @Override
                public R visitPackage(PackageElement e, P param) {
                    return visitElement(PackageElement.class, e, param);
                }

                @Override
                public R visitType(TypeElement e, P param) {
                    return visitElement(TypeElement.class, e, param);
                }

                @Override
                public R visitVariable(VariableElement e, P param) {
                    return aggregate(
                            visitElement(VariableElement.class, e, param));
                }

                @Override
                public R visitExecutable(ExecutableElement e, P param) {
                    return aggregate(
                            visitElement(ExecutableElement.class, e, param),
                            e.getParameters().stream().map(pe -> pe.accept(this, param)));
                }

                @Override
                public R visitTypeParameter(TypeParameterElement e, P param) {
                    return aggregate(
                            visitElement(TypeParameterElement.class, e, param),
                            e.getGenericElement().accept(this, param));

                }

                private <E extends Element> R visitElement(Class<E> cls, E element, P param) {
                    if (!filter.test(element, param)) {
                        return null;
                    }

                    BiFunction<E, P, R> func = listenerOf(cls);

                    return visitedElements.computeIfAbsent(
                            element,
                            _element -> aggregate(
                            aggregate(onAny.apply(element, param), func.apply(element, param)),
                            element.getEnclosedElements().stream().map(el -> el.accept(this, param))));
                }
            };
        }

        @Override
        protected R accept(Element element, ElementVisitor<R, P> visitor, P param) {
            return element.accept(visitor, param);
        }
    }
}
