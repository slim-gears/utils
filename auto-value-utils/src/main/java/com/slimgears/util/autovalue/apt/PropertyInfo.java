package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreTypes;
import com.google.auto.value.AutoValue;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.data.*;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
public abstract class PropertyInfo implements HasName, HasType, HasAnnotations {
    private static final Pattern namePattern = Pattern.compile("^(get)|(is)(?<name>[A-Z]\\w*)$");

    public abstract ExecutableElement element();
    public abstract ImmutableList<MethodInfo> builderMethods();

    public DeclaredType propertyType() {
        return MoreTypes.asDeclared(element().getReturnType());
    }

    public boolean hasBuilder() {
        return PropertyUtils.hasBuilder(element());
    }

    public TypeInfo builderType() {
        return TypeInfo.of(PropertyUtils.builderTypeFor(propertyType()));
    }

    public static Builder builder() {
        return new AutoValue_PropertyInfo.Builder();
    }

    public static PropertyInfo of(DeclaredType declaredType, ExecutableElement element) {
        ExecutableType executableType = MoreTypes.asExecutable(Environment.instance().types().asMemberOf(declaredType, element));
        return builder()
                .name(propertyName(element.getSimpleName().toString()))
                .element(element)
                .type(executableType.getReturnType())
                .annotationsFromElement(element)
                .addBuilderMethods(PropertyUtils.builderMethods(executableType))
                .build();
    }

    private static String propertyName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return (matcher.matches())
                ? matcher.group("name")
                : name;
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<PropertyInfo>,
            HasName.Builder<Builder>,
            HasType.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
        Builder element(ExecutableElement element);
        ImmutableList.Builder<MethodInfo> builderMethodsBuilder();

        default Builder addBuilderMethod(MethodInfo methodInfo) {
            builderMethodsBuilder().add(methodInfo);
            return this;
        }

        default Builder addBuilderMethods(Iterable<MethodInfo> methodInfo) {
            builderMethodsBuilder().addAll(methodInfo);
            return this;
        }
    }
}
