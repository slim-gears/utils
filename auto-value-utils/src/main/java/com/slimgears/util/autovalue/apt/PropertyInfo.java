package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreTypes;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.slimgears.apt.data.*;
import com.slimgears.util.autovalue.annotations.Key;
import com.slimgears.util.autovalue.annotations.Reference;

import javax.annotation.Nullable;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
public abstract class PropertyInfo implements HasName, HasType, HasAnnotations {
    private static final Pattern namePattern = Pattern.compile("^((get)|(is))(?<name>[A-Z]\\w*)$");
    private static final ImmutableSet<String> numericTypes = ImmutableSet.<String>builder()
            .add(Byte.class.getName(), byte.class.getName())
            .add(Short.class.getName(), short.class.getName())
            .add(Integer.class.getName(), int.class.getName())
            .add(Long.class.getName(), long.class.getName())
            .add(Double.class.getName(), double.class.getName())
            .add(Float.class.getName(), float.class.getName())
            .build();

    private static final ImmutableSet<String> comparableTypes = ImmutableSet.<String>builder()
            .add(Date.class.getName())
            .build();

    private static final ImmutableSet<String> booleanTypes = ImmutableSet.<String>builder()
            .add(Boolean.class.getName(), boolean.class.getName())
            .build();

    public abstract ExecutableType executableType();
    public abstract ExecutableElement executableElement();
    public abstract TypeMirror propertyType();
    public abstract ImmutableList<MethodInfo> builderMethods();
    public abstract String setterName();
    public abstract String getterName();
    public boolean isOptional() {
        return executableElement().getAnnotation(Nullable.class) != null;
    }
    public boolean isKey() { return executableElement().getAnnotation(Key.class) != null; }
    public boolean isReferenceProperty() { return executableElement().getAnnotation(Reference.class) != null; }
    public boolean isStringProperty() { return String.class.getName().equals(propertyType().toString()); }
    public boolean isComparableProperty() { return comparableTypes.contains(propertyType().toString()); }
    public boolean isNumericProperty() { return numericTypes.contains(propertyType().toString()); }
    public boolean isBooleanProperty() { return booleanTypes.contains(propertyType().toString()); }

    public boolean hasBuilder() {
        return PropertyUtils.hasBuilder(executableElement());
    }

    public TypeInfo builderType() {
        return TypeInfo.of(PropertyUtils.builderTypeFor(MoreTypes.asDeclared(propertyType())));
    }

    public static Builder builder() {
        return new AutoValue_PropertyInfo.Builder();
    }

    public static PropertyInfo create(DeclaredType declaredType, ExecutableElement element, boolean usePrefixes) {
        ExecutableType executableType = MoreTypes.asExecutable(Environment.instance().types().asMemberOf(declaredType, element));

        String getterName = element.getSimpleName().toString();
        String propertyName = propertyName(getterName);
        String setterName = usePrefixes ? "set" + capitalize(propertyName) : getterName;

        PropertyInfo propertyInfo = builder()
                .name(propertyName)
                .getterName(getterName)
                .setterName(setterName)
                .executableElement(element)
                .executableType(executableType)
                .propertyType(executableType.getReturnType())
                .type(executableType.getReturnType())
                .annotationsFromElement(element)
                .addBuilderMethods(PropertyUtils.builderMethods(executableType))
                .build();

        return propertyInfo;
    }

    static String propertyName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return (matcher.matches())
                ? decapitalize(matcher.group("name"))
                : name;
    }

    private static String capitalize(String name) {
        return Strings.isNullOrEmpty(name) ? name : Character.toUpperCase(name.charAt(0)) + name.substring(1);
    }

    private static String decapitalize(String name) {
        return Strings.isNullOrEmpty(name) ? name : Character.toLowerCase(name.charAt(0)) + name.substring(1);
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<PropertyInfo>,
            HasName.Builder<Builder>,
            HasType.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
        Builder executableType(ExecutableType executableType);
        Builder executableElement(ExecutableElement executableElement);
        Builder propertyType(TypeMirror propertyType);
        ImmutableList.Builder<MethodInfo> builderMethodsBuilder();
        Builder setterName(String setter);
        Builder getterName(String getter);

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
