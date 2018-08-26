/**
 *
 */
package com.slimgears.apt.data;

import com.google.common.collect.ImmutableList;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.Element;

public interface HasAnnotations {
    ImmutableList<AnnotationInfo> annotations();

    interface Builder<B extends Builder<B>> {
        ImmutableList.Builder<AnnotationInfo> annotationsBuilder();

        default B annotationsFromElement(Element element) {
            element.getAnnotationMirrors().forEach(this::annotation);

            //noinspection unchecked
            return (B)this;
        }

        default B annotation(AnnotationMirror annotationMirror) {
            return annotation(AnnotationInfo.of(annotationMirror));
        }

        default B annotation(AnnotationInfo annotation) {
            annotationsBuilder().add(annotation);
            //noinspection unchecked
            return (B)this;
        }
    }
}
