package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.MethodInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.util.stream.Optionals;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.ExecutableType;
import javax.lang.model.type.TypeMirror;
import java.util.Collections;
import java.util.Optional;

import static com.slimgears.util.stream.Streams.ofType;

public class PropertyUtils {
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
                .filter(ee -> Optional
                        .of(ee.getReturnType())
                        .flatMap(Optionals.ofType(DeclaredType.class))
                        .map(DeclaredType::asElement)
                        .map(el -> el.equals(builderType))
                        .orElse(false))
                .map(ee -> MethodInfo.create(ee, builderDeclaredType))
                .collect(ImmutableList.toImmutableList());
    }
}
