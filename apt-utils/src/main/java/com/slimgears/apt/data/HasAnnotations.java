package com.slimgears.apt.data;

import com.google.common.collect.ImmutableList;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;
import java.lang.annotation.Annotation;

public interface HasAnnotations {
    ImmutableList<AnnotationInfo> annotations();

    default AnnotationInfo getAnnotation(TypeInfo atype) {
        return annotations()
                .stream()
                .filter(a -> a.type().equals(atype))
                .findFirst()
                .orElse(null);
    }

    default AnnotationInfo getAnnotation(Class<? extends Annotation> aclass) {
        return getAnnotation(TypeInfo.of(aclass));
    }

    default boolean hasAnnotation(TypeInfo atype) {
        return annotations().stream().anyMatch(a -> a.type().equals(atype));
    }

    default boolean hasAnnotation(Class<? extends Annotation> aclass) {
        return hasAnnotation(TypeInfo.of(aclass));
    }

    @SuppressWarnings("unchecked")
    interface Builder<B extends Builder<B>> {
        B annotations(ImmutableList<AnnotationInfo> annotations);
        ImmutableList.Builder<AnnotationInfo> annotationsBuilder();

        default B annotationsFromElement(Element element) {
            element.getAnnotationMirrors().forEach(this::annotation);
            return (B)this;
        }

        default B annotation(AnnotationMirror annotationMirror) {
            return annotation(AnnotationInfo.of(annotationMirror));
        }

        default B annotation(AnnotationInfo annotation) {
            annotationsBuilder().add(annotation);
            return (B)this;
        }
    }
}
