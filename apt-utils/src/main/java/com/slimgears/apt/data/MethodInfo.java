package com.slimgears.apt.data;

import com.google.auto.value.AutoValue;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.List;
import java.util.stream.IntStream;

@AutoValue
public abstract class MethodInfo implements HasName, HasAnnotations, HasParameters, HasTypeParameters {
    public abstract TypeInfo returnType();

    public static Builder builder() {
        return new AutoValue_MethodInfo.Builder();
    }

    public static MethodInfo of(ExecutableElement executableElement) {
        Builder builder = builder()
                .name(executableElement.getSimpleName().toString())
                .paramsFromMethod(executableElement)
                .typeParamsFromMethod(executableElement)
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
                .annotationsFromElement(element)
                .typeParamsFromMethod(executableType)
                .returnType(TypeInfo.of(executableType.getReturnType()));

        List<? extends VariableElement> paramElements = element.getParameters();
        List<? extends TypeMirror> paramTypes = executableType.getParameterTypes();

        IntStream.range(0, paramElements.size())
                .mapToObj(i -> ParamInfo
                        .builder()
                        .name(paramElements.get(i).getSimpleName().toString())
                        .type(TypeInfo.of(paramTypes.get(i)))
                        .annotationsFromElement(paramElements.get(i))
                        .build())
                .forEach(builder::addParam);

        return builder.build();
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<MethodInfo>,
            HasName.Builder<Builder>,
            HasParameters.Builder<Builder>,
            HasAnnotations.Builder<Builder>,
            HasTypeParameters.Builder<Builder> {
        Builder returnType(TypeInfo type);
    }
}
