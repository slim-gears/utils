/**
 *
 */
package com.slimgears.apt.data;

import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Type;

public interface HasType {
    TypeInfo type();

    interface Builder<B extends Builder<B>> {
        B type(TypeInfo type);

        default B type(Type type) {
            return type(TypeInfo.of(type));
        }

        default B type(TypeMirror typeMirror) {
            return type(TypeInfo.of(typeMirror));
        }

        default B type(TypeElement typeElement) {
            return type(TypeInfo.of(typeElement));
        }
    }
}
