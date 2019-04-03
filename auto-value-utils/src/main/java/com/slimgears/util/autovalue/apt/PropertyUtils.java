package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableSet;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.HasName;
import com.slimgears.apt.data.MethodInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.util.autovalue.annotations.Reference;
import com.slimgears.util.stream.Optionals;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeKind;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.type.TypeVariable;
import javax.lang.model.util.SimpleTypeVisitor8;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;
import java.util.stream.Collectors;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;

public class PropertyUtils {
    private static final ImmutableSet<String> numericTypes = ImmutableSet.<String>builder()
            .add(Byte.class.getName(), byte.class.getName())
            .add(Short.class.getName(), short.class.getName())
            .add(Integer.class.getName(), int.class.getName())
            .add(Long.class.getName(), long.class.getName())
            .add(Double.class.getName(), double.class.getName())
            .add(Float.class.getName(), float.class.getName())
            .build();

    public static TypeElement returnTypeElement(ExecutableElement element) {
        return Optional.of(element)
                .map(ExecutableElement::getReturnType)
                .flatMap(Optionals.ofType(DeclaredType.class))
                .map(DeclaredType::asElement)
                .flatMap(Optionals.ofType(TypeElement.class))
                .orElse(null);
    }

    public static Optional<TypeElement> builderTypeFor(TypeElement typeElement) {
        return typeElement
                .getEnclosedElements()
                .stream()
                .flatMap(ofType(TypeElement.class))
                .filter(t -> "Builder".equals(t.getSimpleName().toString()))
                .filter(t -> typeElement.getTypeParameters().size() == t.getTypeParameters().size())
                .findAny();
    }

    public static DeclaredType builderTypeFor(DeclaredType type) {
        return builderTypeFor(MoreElements.asType(type.asElement()))
                .map(te -> Environment.instance().types().getDeclaredType(te, type.getTypeArguments().toArray(new TypeMirror[0])))
                .orElse(null);
    }

    private static boolean propertyHasPrefix(ExecutableElement element) {
        String getterName = element.getSimpleName().toString();
        return !PropertyInfo.propertyName(getterName).equals(getterName);
    }

    public static Collection<PropertyInfo> getProperties(DeclaredType type) {
        Collection<ExecutableElement> elements = MoreElements
                .getLocalAndInheritedMethods(
                        MoreTypes.asTypeElement(type),
                        Environment.instance().types(),
                        Environment.instance().elements())
                .stream()
                .flatMap(PropertyUtils::isPropertyMethod)
                .collect(Collectors.toList());

        boolean hasPrefix = elements.stream()
                .allMatch(PropertyUtils::propertyHasPrefix);

        return elements
                .stream()
                .map(ee -> PropertyInfo.create(type, ee, hasPrefix))
                .sorted(Comparator.comparing(HasName::name))
                .collect(Collectors.toList());
    }

    private static Stream<ExecutableElement> isPropertyMethod(ExecutableElement executableElement) {
        return Stream
                .of(executableElement)
                .filter(ElementUtils::isAbstract)
                .filter(ElementUtils::isNotStatic)
                .filter(ElementUtils::isPublic)
                .filter(element -> element.getParameters().isEmpty());
    }


    public static boolean hasBuilder(TypeElement element) {
        return builderTypeFor(element).isPresent();
    }

    public static boolean hasBuilder(ExecutableElement element) {
        return ElementUtils
                .toTypeElement(element.getReturnType())
                .anyMatch(PropertyUtils::hasBuilder);
    }

    public static Iterable<MethodInfo> builderMethods(ExecutableType propertyExecutable) {
        if (!(propertyExecutable.getReturnType() instanceof DeclaredType)) {
            return Collections.emptyList();
        }

        DeclaredType propertyType = (DeclaredType)propertyExecutable.getReturnType();

        TypeElement propertyTypeElement = MoreElements.asType(propertyType.asElement());
        TypeElement builderType = builderTypeFor(propertyTypeElement).orElse(null);
        if (builderType == null) {
            return Collections.emptyList();
        }

        DeclaredType builderDeclaredType = Environment.instance().types()
                .getDeclaredType(builderType, propertyType.getTypeArguments().toArray(new TypeMirror[0]));

        return builderType
                .getEnclosedElements()
                .stream()
                .flatMap(ofType(ExecutableElement.class))
                .filter(ElementUtils::isPublic)
                .filter(ElementUtils::isNotStatic)
                .filter(ee -> Optional
                        .of(ee.getReturnType())
                        .flatMap(Optionals.ofType(DeclaredType.class))
                        .map(DeclaredType::asElement)
                        .map(el -> el.equals(builderType))
                        .orElse(false))
                .map(ee -> MethodInfo.create(ee, builderDeclaredType))
                .sorted(Comparator.comparing(HasName::name))
                .collect(ImmutableList.toImmutableList());
    }

    public static boolean isReference(ExecutableElement propertyElement) {
        return propertyElement.getAnnotation(Reference.class) != null;
    }

    public static boolean isString(TypeMirror type) {
        return MoreTypes.isTypeOf(String.class, type);
    }

    public static boolean isNumeric(TypeMirror type) {
        return (type.getKind().isPrimitive() && type.getKind() != TypeKind.BOOLEAN)
            || ElementUtils.hasInterface(type, Number.class, Comparable.class);
    }

    public static boolean isComparable(TypeMirror type) {
        return ElementUtils.hasInterface(type, Comparable.class);
    }

    public static boolean isBoolean(TypeMirror type) {
        return type.getKind() == TypeKind.BOOLEAN
                || type.getKind() == TypeKind.DECLARED && MoreTypes.isTypeOf(Boolean.class, type);
    }

    public static boolean isCollection(TypeMirror type) {
        return ElementUtils.hasInterface(type, Collection.class);
    }

    public static TypeInfo collectionElementType(TypeMirror type) {
        AtomicReference<TypeInfo> elementType = new AtomicReference<>();
        Map<String, TypeMirror> argsMap = new HashMap<>();
        new SimpleTypeVisitor8<Void, Void>() {
            @Override
            public Void visitDeclared(DeclaredType t, Void aVoid) {
                List<? extends TypeMirror> args = t.getTypeArguments();
                List<? extends TypeParameterElement> params = MoreTypes.asTypeElement(t).getTypeParameters();
                IntStream.range(0, args.size())
                        .forEach(i -> {
                            TypeMirror arg = args.get(i);
                            if (arg.getKind() == TypeKind.TYPEVAR) {
                                arg = argsMap.get(((TypeVariable)arg).asElement().getSimpleName().toString());
                            }
                            argsMap.put(params.get(i).getSimpleName().toString(), arg);
                        });

                MoreTypes.asTypeElement(t).getInterfaces().forEach(this::visit);
                if (MoreTypes.isTypeOf(Collection.class, t)) {
                    TypeMirror arg = args.get(0);
                    if (arg.getKind() == TypeKind.TYPEVAR) {
                        arg = Optional.ofNullable(argsMap.get(((TypeVariable)arg).asElement().getSimpleName().toString()))
                                .orElse(arg);
                    }
                    elementType.set(TypeInfo.of(arg));
                }

                return null;
            }
        }.visit(type);

        return Optional.ofNullable(elementType.get()).orElseGet(() -> TypeInfo.of(Object.class));
    }
}
