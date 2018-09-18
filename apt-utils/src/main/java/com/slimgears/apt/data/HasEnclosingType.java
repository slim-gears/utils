package com.slimgears.apt.data;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Type;

public interface HasEnclosingType {
    @Nullable TypeInfo enclosingType();
    default boolean hasEnclosingType() {
        return enclosingType() != null;
    }

    interface Builder<B extends Builder<B>> {
        B enclosingType(TypeInfo type);

        default B enclosingType(Type type) {
            return enclosingType(TypeInfo.of(type.getTypeName()));
        }

        default B enclosingType(TypeMirror type) {
            return enclosingType(TypeInfo.of(type.toString()));
        }

        default B enclosingType(TypeElement type) {
            return enclosingType(TypeInfo.of(type.getQualifiedName().toString()));
        }
    }
}
