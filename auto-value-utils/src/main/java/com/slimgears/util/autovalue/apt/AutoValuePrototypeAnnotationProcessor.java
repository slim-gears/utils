package com.slimgears.util.autovalue.apt;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Sets;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.JavaUtils;
import com.slimgears.apt.util.TemplateEvaluator;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.apt.extensions.Annotator;
import com.slimgears.util.autovalue.apt.extensions.CompositeAnnotator;
import com.slimgears.util.autovalue.apt.extensions.CompositeExtension;
import com.slimgears.util.autovalue.apt.extensions.Extension;
import com.slimgears.util.autovalue.apt.extensions.Extensions;
import com.slimgears.util.stream.Lazy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Name;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.autovalue.annotations.AutoValuePrototype")
public class AutoValuePrototypeAnnotationProcessor extends AbstractAnnotationProcessor {
    private final static Logger log = LoggerFactory.getLogger(AutoValuePrototypeAnnotationProcessor.class);
    private final Lazy<ImmutableMultimap<String, Extension>> extensionMap;
    private final Lazy<ImmutableMultimap<String, Annotator>> annotatorMap;
    private final Map<String, MetaAnnotationInfo> metaAnnotationInfoMap = new HashMap<>();

    public static class MetaAnnotationInfo {
        final AutoValuePrototype prototypeAnnotation;
        final Extension extension;
        final Annotator annotator;

        MetaAnnotationInfo(TypeElement typeElement,
                           Collection<Extension> extensions,
                           Collection<Annotator> annotators) {
            this.prototypeAnnotation = typeElement.getAnnotation(AutoValuePrototype.class);
            this.extension = CompositeExtension.of(extensions);
            this.annotator = CompositeAnnotator.of(annotators);
        }
    }

    public AutoValuePrototypeAnnotationProcessor() {
        this.extensionMap = Lazy.of(() -> Extensions.loadExtensions(Extension.class));
        this.annotatorMap = Lazy.of(() -> Extensions.loadExtensions(Annotator.class));
    }

    @Override
    protected boolean processType(TypeElement annotationElement, TypeElement type) {
        MetaAnnotationInfo metaAnnotation = resolveMetaAnnotation(annotationElement, type);
        if (metaAnnotation == null) {
            delayProcessing("Meta annotation is not available yet");
        }

        if (type.getKind() == ElementKind.ANNOTATION_TYPE) {
            processMetaAnnotation(type, metaAnnotation);
            process(Collections.singleton(type), Environment.instance().roundEnvironment());
        } else {
            processPrototype(type, metaAnnotation);
        }

        return true;
    }

    private void processMetaAnnotation(TypeElement type, MetaAnnotationInfo metaAnnotation) {
        metaAnnotationInfoMap.put(type.getQualifiedName().toString(), metaAnnotation);
    }

    private void processPrototype(TypeElement type, MetaAnnotationInfo metaAnnotation) {
        validatePrototype(type, metaAnnotation);

        DeclaredType declaredType = ElementUtils.toDeclaredType(type);
        //generateInterfaceBuilder(declaredType);

        AutoValuePrototype annotation = metaAnnotation.prototypeAnnotation;
        String pattern = annotation.pattern().isEmpty()
                ? "Prototype"
                : annotation.pattern();


        String targetName = type.getSimpleName().toString().replaceAll(pattern, annotation.value());

        TypeInfo sourceClass = TypeInfo.of(type);
        TypeInfo[] sourceClassParams = sourceClass.typeParams().stream().map(TypeParameterInfo::typeName).map(TypeInfo::of).toArray(TypeInfo[]::new);

        TypeInfo targetClass = TypeInfo.of(sourceClass.packageName() + "." + targetName);
        TypeInfo targetClassDeclaration = targetClass
                .toBuilder()
                .typeParams(sourceClass.typeParams())
                .build();
        TypeInfo targetClassWithParams = targetClass
                .toBuilder()
                .typeParams(sourceClassParams)
                .build();

        Collection<PropertyInfo> properties = PropertyUtils.getProperties(declaredType);

        properties.forEach(p -> {
            String pendingTypes = ElementUtils
                    .findErrors(p.propertyType())
                    .map(TypeInfo::of)
                    .filter(t -> !targetClass.equals(t) && !targetClass.simpleName().equals(t.toString()))
                    .map(TypeInfo::toString)
                    .collect(Collectors.joining(", "));

            if (!pendingTypes.isEmpty()) {
                delayProcessing("Property " + p.name() + ": could not resolve types: " + pendingTypes);
            }
        });

        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());

        List<PropertyInfo> keyProperties = properties.stream().filter(PropertyInfo::isKey).collect(Collectors.toList());
        if (keyProperties.size() > 1) {
            throw new IllegalArgumentException("Type " + type.getSimpleName() + " contains more than 1 key property");
        }
        PropertyInfo keyProperty = keyProperties.isEmpty() ? null : keyProperties.get(0);

        Context context = Context.builder()
                .meta(annotation)
                .sourceClass(sourceClass)
                .sourceElement(type)
                .targetClassDeclaration(targetClassDeclaration)
                .targetClassWithParams(targetClassWithParams)
                .extensions(metaAnnotation.extension)
                .annotators(metaAnnotation.annotator)
                .properties(properties)
                .staticMethods(PropertyUtils.getStaticMethonds(declaredType))
                .imports(importTracker)
                .keyProperty(keyProperty)
                .build();

        importTracker.knownClass(targetClass);

        try {
            TemplateEvaluator.forResource("auto-value.java.vm")
                    .variables(context)
                    .variable("processor", TypeInfo.of(getClass()))
                    .apply(JavaUtils.imports(importTracker))
                    .write(JavaUtils.fileWriter(processingEnv, targetClass));
        } catch (Throwable e) {
            log.error("Error occurred:", e);
        }
    }

    private void validatePrototype(TypeElement type, MetaAnnotationInfo metaAnnotation) {
        AutoValuePrototype annotation = metaAnnotation.prototypeAnnotation;

        Preconditions.checkArgument(ElementUtils.isInterface(type), "AutoValue Prototype should be interface");
        Preconditions.checkArgument(
                !annotation.value().isEmpty() ||
                type.getSimpleName().toString().startsWith("Prototype") ||
                type.getSimpleName().toString().endsWith("Prototype"),
                "AutoValue Prototype name should start or end with 'Prototype'");
    }

    private MetaAnnotationInfo resolveMetaAnnotation(TypeElement annotationElement, TypeElement typeElement) {
        return AutoValuePrototype.class.getName().equals(annotationElement.getQualifiedName().toString())
                ? new MetaAnnotationInfo(
                        typeElement,
                        Extensions.extensionsForType(extensionMap.get(), typeElement),
                        Extensions.extensionsForType(annotatorMap.get(), typeElement))
                : Optional.ofNullable(annotationElement.getQualifiedName())
                .map(Name::toString)
                .map(metaAnnotationInfoMap::get)
                .orElse(null);
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        return Sets.union(super.getSupportedAnnotationTypes(), metaAnnotationInfoMap.keySet());
    }
}
