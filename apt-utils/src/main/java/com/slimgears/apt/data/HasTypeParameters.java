/**
 *
 */
package com.slimgears.apt.data;

import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import java.util.Arrays;
import java.util.Collection;
import java.util.stream.Stream;

public interface HasTypeParameters {
    ImmutableList<TypeParameterInfo> typeParams();

    default boolean hasTypeParams() {
        return !typeParams().isEmpty();
    }

    interface Builder<B extends Builder<B>> {
        ImmutableList.Builder<TypeParameterInfo> typeParamsBuilder();

        default B typeParamsFromTypeMirrors(Collection<? extends TypeMirror> args) {
            args.forEach(this::typeParam);
            //noinspection unchecked
            return (B)this;
        }

        default B typeParams(TypeInfo... types) {
            Stream.of(types)
                    .map(t -> TypeParameterInfo.of("", t))
                    .forEach(this::typeParam);
            //noinspection unchecked
            return (B)this;
        }

        default B typeParams(TypeParameterInfo... params) {
            return typeParams(Arrays.asList(params));
        }

        default B typeParams(Collection<TypeParameterInfo> params) {
            params.forEach(this::typeParam);
            //noinspection unchecked
            return (B)this;
        }

        default B typeParamsFromElements(Collection<? extends TypeParameterElement> params) {
            params.stream().map(TypeParameterInfo::of).forEach(this::typeParam);
            //noinspection unchecked
            return (B)this;
        }

        default B typeParamsFromMethod(ExecutableElement element) {
            return typeParamsFromElements(element.getTypeParameters());
        }

        default B typeParamsFromMethod(ExecutableType executableType) {
            executableType.getTypeVariables().stream().map(TypeParameterInfo::of).forEach(this::typeParam);
            //noinspection unchecked
            return (B)this;
        }

        default B typeParam(String name, TypeInfo type) {
            return typeParam(TypeParameterInfo.of(name, type));
        }

        default B typeParam(TypeParameterInfo param) {
            typeParamsBuilder().add(param);
            //noinspection unchecked
            return (B)this;
        }

        default B typeParam(TypeMirror type) {
            if (type instanceof TypeVariable) {
                return typeParam(TypeParameterInfo.of((TypeVariable)type));
            } else {
                return typeParam(TypeParameterInfo.of("", TypeInfo.of(type)));
            }
        }
    }
}
