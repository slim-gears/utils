package com.slimgears.apt.data;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.slimgears.util.stream.Optionals;

import javax.annotation.Nullable;
import javax.lang.model.element.Element;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Type;
import java.util.Optional;

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
            return enclosingType(TypeInfo.of(type).withoutAnnontations());
        }

        default B enclosingType(TypeElement type) {
            return enclosingType(TypeInfo.of(type).withoutAnnontations());
        }

        @SuppressWarnings("unchecked")
        default B enclosingType(DeclaredType type) {
            enclosingType(HasEnclosingType.enclosingType(type));
            return (B)this;
        }
    }

    static TypeInfo enclosingType(TypeMirror type) {
        return enclosingType(MoreTypes.asDeclared(type));
    }

    static TypeInfo enclosingType(TypeElement type) {
        return enclosingType(type.asType());
    }

    static TypeInfo enclosingType(DeclaredType type) {
        TypeMirror enclosingType = type.getEnclosingType();
        if (enclosingType.equals(type)) {
            return null;
        }

        if (enclosingType.getKind() != TypeKind.NONE) {
            return TypeInfo.of(enclosingType);
        }
        return Optional.of(type.asElement())
                .map(Element::getEnclosingElement)
                .flatMap(Optionals.ofType(TypeElement.class))
                .map(element -> TypeInfo.of(element).withoutAnnontations())
                .orElse(null);
    }
}
