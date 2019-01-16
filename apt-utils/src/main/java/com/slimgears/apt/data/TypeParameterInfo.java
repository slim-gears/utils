package com.slimgears.apt.data;

import com.google.auto.common.MoreTypes;
import com.google.auto.value.AutoOneOf;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.slimgears.util.generic.ScopedInstance;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Optional;

@AutoValue
public abstract class TypeParameterInfo implements HasName, HasType {
    @AutoOneOf(BoundInfo.Kind.class)
    public static abstract class BoundInfo {
        public enum Kind {
            BoundSuper,
            BoundExtends
        }

        public abstract Kind kind();
        public abstract TypeInfo boundSuper();
        public abstract TypeInfo boundExtends();

        public String toString() {
            return kind() == Kind.BoundSuper
                    ? " super " + boundSuper().fullName()
                    : " extends " + boundExtends().fullName();
        }

        public static BoundInfo ofBoundSuper(TypeInfo type) {
            return AutoOneOf_TypeParameterInfo_BoundInfo.boundSuper(type);
        }

        public static BoundInfo ofBoundExtends(TypeInfo type) {
            return AutoOneOf_TypeParameterInfo_BoundInfo.boundExtends(type);
        }
    }

    @Nullable public abstract BoundInfo bounding();

    public static TypeParameterInfo.Builder builder() {
        return new AutoValue_TypeParameterInfo.Builder();
    }

    public static TypeParameterInfo of(TypeParameterElement element) {
        return of(element.asType());
    }

    public static TypeParameterInfo of(TypeMirror typeMirror) {
        if (typeMirror instanceof TypeVariable) {
            return of(MoreTypes.asTypeVariable(typeMirror));
        }
        return builder()
                .name("")
                .type(typeMirror)
                .build();
    }

    public static TypeParameterInfo of(TypeVariable typeVariable) {
        try (ScopedInstance.Closeable ignored = TypeParameterScope.scope()) {
            Builder builder = builder()
                    .name(typeVariable.asElement().getSimpleName().toString())
                    .type(typeVariable);

            if (TypeParameterScope.current().addIfNotExists(typeVariable)) {
                builder.bounding(typeVariable);
            }

            return builder.build();
        }
    }

    public static TypeParameterInfo of(String name, TypeInfo type) {
        return builder().name(name).type(type).build();
    }

    public String fullDeclaration() {
        return Strings.isNullOrEmpty(name())
                ? typeName()
                : name() + Optional.ofNullable(bounding()).map(BoundInfo::toString).orElse("");
    }

    public String typeName() {
        return type().isWildcard()
                ? type().name() + Optional.ofNullable(bounding()).map(BoundInfo::toString).orElse("")
                : type().fullName();
    }

    @AutoValue.Builder
    public interface Builder extends InfoBuilder<TypeParameterInfo>, HasName.Builder<Builder>, HasType.Builder<Builder> {
        Builder bounding(BoundInfo boundInfo);

        default Builder bounding(TypeVariable typeVariable) {
            return Optional
                    .ofNullable(typeVariable.getLowerBound())
                    .filter(t -> t.getKind() != TypeKind.NULL)
                    .map(this::superBounding)
                    .orElseGet(() -> Optional
                            .ofNullable(typeVariable.getUpperBound())
                            .filter(t -> t.getKind() != TypeKind.NULL)
                            .map(this::extendsBounding)
                            .orElse(this));
        }

        default Builder extendsBounding(TypeMirror type) {
            if ("java.lang.Object".equals(type.toString())) {
                return this;
            }
            return extendsBounding(TypeInfo.of(type));
        }

        default Builder extendsBounding(TypeInfo type) {
            return bounding(BoundInfo.ofBoundExtends(type));
        }

        default Builder superBounding(TypeMirror type) {
            return superBounding(TypeInfo.of(type));
        }

        default Builder superBounding(TypeInfo type) {
            return bounding(BoundInfo.ofBoundSuper(type));
        }
    }
}
