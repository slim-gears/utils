/**
 *
 */
package com.slimgears.apt.data;

import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;

public interface HasMethods {
    ImmutableList<MethodInfo> methods();

    default ImmutableList<MethodInfo> constructors() {
        return methods()
                .stream()
                .filter(m -> "<init>".equals(m.name()))
                .collect(ImmutableList.toImmutableList());
    }

    @SuppressWarnings("unchecked")
    interface Builder<B extends Builder<B>> {
        ImmutableList.Builder<MethodInfo> methodsBuilder();

        default B method(ExecutableElement element) {
            return method(MethodInfo.of(element));
        }

        default B method(MethodInfo method) {
            methodsBuilder().add(method);
            return (B)this;
        }
    }
}
