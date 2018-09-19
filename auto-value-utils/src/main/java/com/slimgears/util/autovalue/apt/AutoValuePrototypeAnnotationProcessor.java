package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.*;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Collection;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.slimgears.util.stream.Streams.ofType;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.autovalue.annotations.AutoValuePrototype")
public class AutoValuePrototypeAnnotationProcessor extends AbstractAnnotationProcessor {
//    private final Collection<DeclaredType> processedElements = new HashSet<>();
    private final ImmutableMultimap<String, Annotator> valueAnnotators;

    public AutoValuePrototypeAnnotationProcessor() {
        ImmutableMultimap.Builder<String, Annotator> builder = ImmutableMultimap.builder();
        StreamSupport
                .stream(ServiceLoader.load(Annotator.class, getClass().getClassLoader()).spliterator(), false)
                .filter(service -> service.getClass().isAnnotationPresent(AnnotatorId.class))
                .forEach(service -> {
                    String[] keys = service.getClass().getAnnotation(AnnotatorId.class).value();
                    Arrays.asList(keys).forEach(key -> builder.put(key, service));
                });
        valueAnnotators = builder.build();
    }

    @Override
    protected boolean processType(TypeElement annotationElement, TypeElement type) {
        validatePrototype(type);

        DeclaredType declaredType = ElementUtils.toDeclaredType(type);
        generateInterfaceBuilder(declaredType);

        AutoValuePrototype annotation = type.getAnnotation(AutoValuePrototype.class);
        String targetName = annotation.value().isEmpty()
                ? type.getSimpleName().toString().replace("Prototype", "")
                : annotation.value();

        TypeInfo sourceClass = TypeInfo.of(type);
        TypeInfo targetClass = TypeInfo.of(sourceClass.packageName() + "." + targetName);

        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());

        try {
            TemplateEvaluator.forResource("auto-value.java.vm")
                    .variable("processor", TypeInfo.of(getClass()))
                    .variable("sourceClass", sourceClass)
                    .variable("targetClass", targetClass)
                    .variable("imports", importTracker)
                    .apply(JavaUtils.imports(importTracker))
                    .write(JavaUtils.fileWriter(processingEnv, targetClass));
        } catch (Throwable e) {
            log.error("Error occurred: {}", e);
            return true;
        }

        return true;
    }

    private Annotator getAnnotators(String[] annotatorIds) {
        return Arrays
                .stream(annotatorIds)
                .flatMap(id -> valueAnnotators.get(id).stream())
                .distinct()
                .reduce(Annotator::combine)
                .orElse(Annotator.empty);
    }

    private void generateInterfaceBuilder(DeclaredType type) {
        Collection<BuilderInfo> builders = getBuilders(type);

        TypeInfo sourceClass = TypeInfo.of(type);
        String targetName = sourceClass.simpleName() + "Builder";
        TypeInfo targetClass = TypeInfo.of(sourceClass.packageName() + "." + targetName);
        Collection<PropertyInfo> properties = getProperties(type);
        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());

        TemplateEvaluator.forResource("auto-value-builder.java.vm")
                .variable("utils", new TemplateUtils())
                .variable("builders", builders)
                .variable("properties", properties)
                .variable("processor", TypeInfo.of(getClass()))
                .variable("sourceClass", sourceClass)
                .variable("targetClass", targetClass)
                .variable("imports", importTracker)
                .apply(JavaUtils.imports(importTracker))
                .write(JavaUtils.fileWriter(processingEnv, targetClass));
    }

    private Collection<BuilderInfo> getBuilders(DeclaredType type) {
        return ElementUtils.getHierarchy(type)
                .flatMap(valType -> ElementUtils.toTypeElement(valType)
                        .map(TypeElement::getEnclosedElements)
                        .flatMap(Collection::stream)
                        .flatMap(ofType(TypeElement.class))
                        .filter(isValidBuilder(valType))
                        .map(te -> BuilderInfo.create(valType, MoreTypes.asDeclared(te.asType()))))
                .collect(Collectors.toList());
    }

    private Predicate<TypeElement> isValidBuilder(DeclaredType type) {
        return te -> isValidBuilderName(te) && hasValidTypeParams(type, te);
    }

    private boolean isValidBuilderName(TypeElement typeElement) {
        return "Builder".equals(typeElement.getSimpleName().toString());
    }

    private boolean hasValidTypeParams(DeclaredType valType, TypeElement typeElement) {
        int paramSize = typeElement.getTypeParameters().size();
        return paramSize == valType.getTypeArguments().size() + 1 &&
                hasValidBuilderBounds(typeElement, typeElement.getTypeParameters().get(paramSize - 1));
    }

    private boolean hasValidBuilderBounds(TypeElement builderType, TypeParameterElement typeParamElement) {
        if (typeParamElement.getBounds().size() != 1) {
            return false;
        }
        TypeMirror bound = typeParamElement.getBounds().get(0);
        if (!(bound instanceof DeclaredType)) {
            return false;
        }
        DeclaredType boundDeclaredType = (DeclaredType) bound;
        TypeElement extendsBound = MoreTypes.asTypeElement(boundDeclaredType);
        return extendsBound.equals(builderType);
    }

    private boolean propertyHasPrefix(ExecutableElement element) {
        String getterName = element.getSimpleName().toString();
        return !PropertyInfo.propertyName(getterName).equals(getterName);
    }

    private Collection<PropertyInfo> getProperties(DeclaredType type) {
        boolean hasPrefix = ElementUtils.getHierarchy(type)
                .flatMap(dt -> ElementUtils
                        .toTypeElement(dt)
                        .map(TypeElement::getEnclosedElements)
                        .flatMap(Collection::stream)
                        .flatMap(ofType(ExecutableElement.class))
                        .filter(ElementUtils::isAbstract)
                        .filter(ElementUtils::isNotStatic)
                        .filter(ElementUtils::isPublic)
                        .filter(element -> element.getParameters().isEmpty()))
                .allMatch(this::propertyHasPrefix);

        return ElementUtils.getHierarchy(type)
                .flatMap(dt -> ElementUtils
                        .toTypeElement(dt)
                        .map(TypeElement::getEnclosedElements)
                        .flatMap(Collection::stream)
                        .flatMap(ofType(ExecutableElement.class))
                        .filter(ElementUtils::isAbstract)
                        .filter(ElementUtils::isNotStatic)
                        .filter(ElementUtils::isPublic)
                        .filter(element -> element.getParameters().isEmpty())
                        .map(ee -> PropertyInfo.create(dt, ee, hasPrefix)))
                .collect(Collectors.toList());
    }

    private void validatePrototype(TypeElement type) {
        AutoValuePrototype annotation = type.getAnnotation(AutoValuePrototype.class);

        Preconditions.checkArgument(ElementUtils.isInterface(type), "AutoValue Prototype should be interface");
        Preconditions.checkArgument(
                !annotation.value().isEmpty() ||
                type.getSimpleName().toString().startsWith("Prototype") ||
                type.getSimpleName().toString().endsWith("Prototype"),
                "AutoValue Prototype name should start or end with 'Prototype'");
    }
}
