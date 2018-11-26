package com.slimgears.apt.data;

import com.google.auto.common.MoreTypes;
import com.google.auto.common.MoreElements;
import com.google.auto.value.AutoValue;
import com.google.common.base.Strings;
import com.google.common.collect.ImmutableMap;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.TypeInfoParserAdapter;
import com.slimgears.util.generic.ScopedInstance;

import javax.lang.model.element.Element;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.NoType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import java.lang.reflect.Type;
import java.util.Comparator;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;
import static java.util.Objects.requireNonNull;

@AutoValue
public abstract class TypeInfo implements HasName, HasEnclosingType, HasMethods, HasAnnotations, HasTypeParameters {
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
        String packageName = packageName();
        return (Strings.isNullOrEmpty(packageName) ? name() : name().substring(packageName.length() + 1)) + dimensionsToString();
    }

    public String erasureName() {
        return name() + dimensionsToString();
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
        return register(TypeInfoParserAdapter.toTypeInfo(fullName));
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

    public static TypeInfo of(DeclaredType declaredType) {
        return builder()
                .name(MoreElements.asType(declaredType.asElement()).getQualifiedName().toString())
                .enclosingTypeFrom(declaredType)
                .typeParamsFromTypeMirrors(declaredType.getTypeArguments())
                .build();
    }

    public static TypeInfo of(Type type) {
        return register(of(type.getTypeName()));
    }

    public static TypeInfo of(TypeElement typeElement) {
        DeclaredType declaredType = ElementUtils.toDeclaredType(typeElement);
        Builder builder = builder()
                .name(typeElement.getQualifiedName().toString())
                .annotationsFromElement(typeElement)
                .typeParams(typeElement.getTypeParameters());

        if (typeElement.getEnclosingElement() instanceof TypeElement) {
            builder.enclosingType(MoreElements.asType(typeElement.getEnclosingElement()));
        }

        typeElement.getEnclosedElements()
                .stream()
                .flatMap(ofType(ExecutableElement.class))
                .map(m -> MethodInfo.create(m, declaredType))
                .forEach(builder::method);

        return register(builder.build());
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<TypeInfo>,
            HasName.Builder<Builder>,
            HasEnclosingType.Builder<Builder>,
            HasMethods.Builder<Builder>,
            HasTypeParameters.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
        Builder arrayDimensions(int dimensions);
    }

    public TypeInfo asBoxed() {
        return Optional.ofNullable(boxableTypes.get(this)).orElse(this);
    }

    @Override
    public String toString() {
        return fullName().replace('$', '.');
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

    public static ScopedInstance.Closable withRegistrar() {
        return typeRegistrar.scope(new HashMap<>());
    }

    private static TypeInfo register(TypeInfo type) {
        typeRegistrar.current().computeIfAbsent(type.erasureName(), tname -> type.hasTypeParams() ? TypeInfo.of(tname) : type);
        if (!type.hasEnclosingType() && typeRegistrar.current().containsKey(packageName(type.erasureName()))) {
            return type.toBuilder().enclosingType(typeRegistrar.current().get(packageName(type.erasureName()))).build();
        }
        return type;
    }
}
