package com.slimgears.util.autovalue.apt;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.JavaUtils;
import com.slimgears.apt.util.TemplateEvaluator;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.ServiceLoader;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import static com.slimgears.util.stream.Streams.ofType;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.autovalue.annotations.AutoValuePrototype")
public class AutoValuePrototypeAnnotationProcessor extends AbstractAnnotationProcessor {
    private final Collection<TypeElement> processedElements = new HashSet<>();
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
        ensureBuildersForInterfaces(type);

        Collection<PropertyInfo> properties = getProperties(type);

        AutoValuePrototype annotation = type.getAnnotation(AutoValuePrototype.class);

        String targetName = annotation.value().isEmpty()
                ? type.getSimpleName().toString().replace("Prototype", "")
                : annotation.value();

        TypeInfo sourceClass = TypeInfo.of(type);
        TypeInfo targetClass = TypeInfo.of(sourceClass.packageName() + "." + targetName);

        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());

        try {
            TemplateEvaluator.forResource("auto-value.java.vm")
                    .variable("properties", properties)
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

    private void ensureBuildersForInterfaces(TypeElement typeElement) {
        ElementUtils
                .getHierarchy(typeElement)
                .filter(ElementUtils::isInterface)
                .filter(e -> !processedElements.contains(e))
                .forEach(this::generateInterfaceBuilder);
    }

    private void generateInterfaceBuilder(TypeElement type) {
        processedElements.add(type);

        Collection<TypeInfo> interfaces = type
                .getInterfaces()
                .stream()
                .map(TypeInfo::of)
                .collect(Collectors.toList());

        TypeInfo sourceClass = TypeInfo.of(type);
        String targetName = sourceClass.simpleName() + "Builder";
        TypeInfo targetClass = TypeInfo.of(sourceClass.packageName() + "." + targetName);
        Collection<PropertyInfo> properties = getProperties(type);
        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());

        TemplateEvaluator.forResource("auto-value-builder.java.vm")
                .variable("properties", properties)
                .variable("interfaces", interfaces)
                .variable("processor", TypeInfo.of(getClass()))
                .variable("sourceClass", sourceClass)
                .variable("targetClass", targetClass)
                .variable("imports", importTracker)
                .apply(JavaUtils.imports(importTracker))
                .write(JavaUtils.fileWriter(processingEnv, targetClass));
    }

    private Collection<PropertyInfo> getProperties(TypeElement type) {
        return type
                .getEnclosedElements()
                .stream()
                .flatMap(ofType(ExecutableElement.class))
                .filter(ElementUtils::isAbstract)
                .filter(ElementUtils::isNotStatic)
                .filter(ElementUtils::isPublic)
                .filter(element -> element.getParameters().isEmpty())
                .map(PropertyInfo::of)
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
