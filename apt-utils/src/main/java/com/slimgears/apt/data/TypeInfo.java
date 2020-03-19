package com.slimgears.apt.data;

import com.google.auto.common.MoreElements;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.TypeTokenParserAdapter;
import com.slimgears.util.generic.ScopedInstance;

import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static java.util.Objects.requireNonNull;

@SuppressWarnings("WeakerAccess")
@AutoValue
public abstract class TypeInfo implements HasName, HasEnclosingType, HasAnnotations, HasTypeParameters {
    private final static ScopedInstance<Map<String, TypeInfo>> typeRegistrar = ScopedInstance.create(new HashMap<>());

    public final static Comparator<TypeInfo> comparator = Comparator
            .<TypeInfo, String>comparing(TypeInfo::packageName)
            .thenComparing(TypeInfo::simpleName);

    private final static ImmutableMap<TypeInfo, TypeInfo> boxableTypes = ImmutableMap.<TypeInfo, TypeInfo>builder()
            .put(TypeInfo.of(void.class), TypeInfo.of(Void.class))
            .put(TypeInfo.of(boolean.class), TypeInfo.of(Boolean.class))
            .put(TypeInfo.of(short.class), TypeInfo.of(Short.class))
            .put(TypeInfo.of(int.class), TypeInfo.of(Integer.class))
            .put(TypeInfo.of(long.class), TypeInfo.of(Long.class))
            .put(TypeInfo.of(float.class), TypeInfo.of(Float.class))
            .put(TypeInfo.of(double.class), TypeInfo.of(Double.class))
            .put(TypeInfo.of(char.class), TypeInfo.of(Character.class))
            .build();

    public abstract int arrayDimensions();

    public abstract Builder toBuilder();

    public String fullName() {
        String name = typeParams().isEmpty()
                ? name()
                : name() + typeParams().stream()
                        .map(TypeParameterInfo::typeName)
                        .collect(Collectors.joining(", ", "<", ">"));

        return name + dimensionsToString();
    }

    public TypeInfo toSymbolic() {
        return of(fullName());
    }

    public String importName() {
        return hasEnclosingType() ? requireNonNull(enclosingType()).importName() : erasureName();
    }

    public Optional<TypeInfo> elementType() {
        return isArray()
                ? Optional.of(toBuilder().arrayDimensions(0).build())
                : !typeParams().isEmpty()
                        ? Optional.of(typeParams().get(0).type())
                        : Optional.empty();
    }

    public TypeInfo elementTypeOrVoid() {
        return elementType().orElse(TypeInfo.of(Void.class));
    }

    public TypeInfo elementTypeOrSelf() {
        return elementType().orElse(this);
    }

    public String simpleName() {
        return nameWithoutPackage() + dimensionsToString();
    }

    public String erasureName() {
        return name() + dimensionsToString();
    }

    public TypeInfo withoutAnnontations() {
        return annotations().isEmpty()
                ? this
                : toBuilder()
                .annotations(ImmutableList.of())
                .build();
    }

    public TypeInfo erasure() {
        return TypeInfo.of(erasureName());
    }

    public String fullDeclaration() {
        return !hasTypeParams()
                ? fullName()
                : typeParams()
                .stream()
                .map(TypeParameterInfo::fullDeclaration)
                .collect(Collectors.joining(", ", name() + "<", ">"));
    }

    public String nameWithoutPackage() {
        String packageName = packageName();
        return (Strings.isNullOrEmpty(packageName) ? name() : name().substring(packageName.length() + 1));
    }

    public String packageName() {
        return hasEnclosingType() ? requireNonNull(enclosingType()).packageName() : packageName(name());
    }

    public boolean isArray() {
        return arrayDimensions() > 0;
    }

    public boolean is(String name) {
        return name().equals(name);
    }

    public boolean is(Class cls) {
        return name().equals(cls.getName());
    }

    public boolean isOneOf(Class... cls) {
        return Stream.of(cls).anyMatch(this::is);
    }

    public boolean isWildcard() {
        return "?".equals(name());
    }

    public static Builder builder() {
        return new AutoValue_TypeInfo.Builder().arrayDimensions(0);
    }

    public static TypeInfo ofWildcard() {
        return TypeInfo.builder().name("?").build();
    }

    public static TypeInfo ofPrimitive(String name) {
        return TypeInfo.builder().name(name).build();
    }

    public static TypeInfo arrayOf(TypeInfo typeInfo) {
        return arrayOf(typeInfo, 1);
    }

    public static TypeInfo arrayOf(TypeInfo typeInfo, int dimensions) {
        return typeInfo
                .toBuilder()
                .arrayDimensions(typeInfo.arrayDimensions() + dimensions)
                .build();
    }

    public static TypeInfo of(String name, TypeInfo param, TypeInfo... otherParams) {
        return register(builder().name(name)
                .typeParams(param)
                .typeParams(otherParams).build());
    }

    public static TypeInfo of(String fullName) {
        return register(TypeTokenParserAdapter.toTypeInfo(fullName));
    }

    public static TypeInfo of(TypeMirror typeMirror) {
        if (typeMirror instanceof NoType) {
            return TypeInfo.of(void.class);
        } else if (typeMirror instanceof DeclaredType) {
            return register(TypeInfo.of((DeclaredType)(typeMirror)));
        } else {
            return register(of(typeMirror.toString()));
        }
    }

    @SuppressWarnings("UnstableApiUsage")
    public static TypeInfo of(DeclaredType declaredType) {
        TypeInfo enclosingType = HasEnclosingType.enclosingType(declaredType);
        String name = MoreElements.asType(declaredType.asElement()).getQualifiedName().toString();
        if (enclosingType != null) {
            name = name.replace(enclosingType.name() + ".", enclosingType.name() + "$");
        }

        return builder()
                .name(name)
                .enclosingType(enclosingType)
                .typeParamsFromTypeMirrors(declaredType.getTypeArguments())
                .build();
    }

    public static TypeInfo of(Type type) {
        return register(of(type.getTypeName()));
    }

    public static TypeInfo of(TypeElement typeElement) {
        DeclaredType declaredType = ElementUtils.toDeclaredType(typeElement);
        String name = typeElement.getQualifiedName().toString();
        Builder builder = builder()
                .annotationsFromElement(typeElement)
                .typeParamsFromElements(typeElement.getTypeParameters());

        TypeInfo enclosingType = HasEnclosingType.enclosingType(typeElement);
        if (enclosingType != null) {
            name = name.replace(enclosingType.name() + ".", enclosingType.name() + "$");
        }

//        typeElement.getEnclosedElements()
//                .stream()
//                .flatMap(ofType(ExecutableElement.class))
//                .map(m -> MethodInfo.create(m, declaredType))
//                .forEach(builder::method);

        return register(builder
                .enclosingType(enclosingType)
                .name(name)
                .build());
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<TypeInfo>,
            HasName.Builder<Builder>,
            HasEnclosingType.Builder<Builder>,
            HasTypeParameters.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
        Builder arrayDimensions(int dimensions);
    }

    public TypeInfo asBoxed() {
        return Optional.ofNullable(boxableTypes.get(this)).orElse(this);
    }

    @Override
    public String toString() {
        //return fullName().replace('$', '.');
        return fullName();
    }

    public static String packageName(String qualifiedName) {
        qualifiedName = qualifiedName.split("\\$")[0];
        int pos = qualifiedName.lastIndexOf('.');
        return (pos >= 0) ? qualifiedName.substring(0, pos) : "";
    }

    public static String packageName(Name name) {
        return packageName(name.toString());
    }

    private String dimensionsToString() {
        return IntStream.range(0, arrayDimensions()).mapToObj(i -> "[]").collect(Collectors.joining());
    }

    public static ScopedInstance.Closeable withRegistrar() {
        return typeRegistrar.scope(new HashMap<>());
    }

    private static TypeInfo register(TypeInfo type) {
        String erasureName = type.erasureName();
        if (typeRegistrar.current().get(erasureName) == null) {
            TypeInfo newValue = type.hasTypeParams() ? TypeInfo.of(erasureName) : type;
            if (newValue != null) {
                typeRegistrar.current().put(erasureName, newValue);
            }
        }
        if (!type.hasEnclosingType() && typeRegistrar.current().containsKey(packageName(erasureName))) {
            return type.toBuilder().enclosingType(typeRegistrar.current().get(packageName(erasureName))).build();
        }
        return type;
    }
}
