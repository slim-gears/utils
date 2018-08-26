package com.slimgears.apt.data;

import com.google.auto.value.AutoOneOf;
import com.google.auto.value.AutoValue;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeParameterElement;
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
                    ? "? super " + boundSuper().fullName()
                    : "? extends " + boundExtends().fullName();
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
        return of(element.getSimpleName().toString(), TypeInfo.of(element.asType()));
    }

    public static TypeParameterInfo of(TypeVariable typeVariable) {
        return of(typeVariable.asElement().getSimpleName().toString(), TypeInfo.of(typeVariable.asElement().asType()));
    }

    public static TypeParameterInfo of(String name, TypeInfo type) {
        return builder().name(name).type(type).build();
    }

    public String typeName() {
        return type().isWildcard()
                ? Optional.ofNullable(bounding()).map(BoundInfo::toString).orElse("?")
                : type().fullName();
    }

    @AutoValue.Builder
    public interface Builder extends InfoBuilder<TypeParameterInfo>, HasName.Builder<Builder>, HasType.Builder<Builder> {
        Builder bounding(BoundInfo boundInfo);

        default Builder extendsBounding(TypeInfo type) {
            return bounding(BoundInfo.ofBoundExtends(type));
        }

        default Builder superBounding(TypeInfo type) {
            return bounding(BoundInfo.ofBoundSuper(type));
        }
    }
}
