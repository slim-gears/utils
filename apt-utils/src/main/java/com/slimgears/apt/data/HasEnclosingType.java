package com.slimgears.apt.data;

import com.google.auto.common.MoreElements;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
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

        @SuppressWarnings("unchecked")
        default B enclosingType(DeclaredType type) {
            enclosingType(HasEnclosingType.enclosingType(type));
            return (B)this;
        }
    }

    static TypeInfo enclosingType(TypeMirror type) {
        return TypeInfo.of(type.toString());
    }

    static TypeInfo enclosingType(TypeElement type) {
        return TypeInfo.of(type.getQualifiedName().toString());
    }

    static TypeInfo enclosingType(DeclaredType type) {
        TypeMirror enclosingType = type.getEnclosingType();
        if (enclosingType.getKind() != TypeKind.NONE) {
            return enclosingType(enclosingType);
        } else {
            Element element = type.asElement().getEnclosingElement();
            if (element instanceof TypeElement) {
                return enclosingType(MoreElements.asType(element));
            }
        }
        return null;
    }
}
