/**
 *
 */
package com.slimgears.apt.data;

import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;
import java.util.stream.Stream;

public interface HasParameters {
    ImmutableList<ParamInfo> params();

    @SuppressWarnings("unchecked")
    interface Builder<B extends Builder<B>> {
        ImmutableList.Builder<ParamInfo> paramsBuilder();

        default B addParam(ParamInfo param) {
            paramsBuilder().add(param);
            return (B)this;
        }

        default B addParam(String name, TypeInfo type) {
            return addParam(ParamInfo.create(name, type));
        }

        default B params(Stream<ParamInfo> params) {
            params.forEach(this::addParam);
            return (B)this;
        }

        default B paramsFromMethod(ExecutableElement executableElement) {
            executableElement.getParameters().forEach(v -> addParam(ParamInfo.of(v)));
            return (B)this;
        }
    }
}
