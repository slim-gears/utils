package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.IntStream;

@AutoValue
public abstract class MethodInfo implements HasName, HasAnnotations, HasTypeParameters {
    public abstract ImmutableList<ParamInfo> params();
    public abstract TypeInfo returnType();

    public static Builder builder() {
        return new AutoValue_MethodInfo.Builder();
    }

    public static MethodInfo of(ExecutableElement executableElement) {
        Builder builder = builder()
                .name(executableElement.getSimpleName().toString())
                .paramsFromMethod(executableElement)
                .returnType(TypeInfo.of(executableElement.getReturnType()));

        executableElement.getParameters()
                .stream()
                .map(ParamInfo::of)
                .forEach(builder::addParam);

        return builder.build();
    }

    public static MethodInfo create(ExecutableElement element, DeclaredType containingType) {
        ExecutableType executableType = (ExecutableType)Environment.instance().types().asMemberOf(containingType, element);
        Builder builder = builder()
                .name(element.getSimpleName().toString())
                .paramsFromMethod(executableType)
                .returnType(TypeInfo.of(executableType.getReturnType()));

        List<? extends VariableElement> paramElements = element.getParameters();
        List<? extends TypeMirror> paramTypes = executableType.getParameterTypes();

        IntStream.range(0, paramElements.size())
                .mapToObj(i -> ParamInfo.create(paramElements.get(i).getSimpleName().toString(), TypeInfo.of(paramTypes.get(i))))
                .forEach(builder::addParam);

        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<MethodInfo>,
            HasName.Builder<Builder>,
            HasAnnotations.Builder<Builder>,
            HasTypeParameters.Builder<Builder> {
        ImmutableList.Builder<ParamInfo> paramsBuilder();
        Builder returnType(TypeInfo type);

        default Builder addParam(ParamInfo param) {
            paramsBuilder().add(param);
            return this;
        }

        default Builder addParam(String name, TypeInfo type) {
            return addParam(ParamInfo.create(name, type));
        }
    }
}
