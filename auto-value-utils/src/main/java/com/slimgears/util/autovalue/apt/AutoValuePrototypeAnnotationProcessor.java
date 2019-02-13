package com.slimgears.util.autovalue.apt;

import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMultimap;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.JavaUtils;
import com.slimgears.apt.util.TemplateEvaluator;
import com.slimgears.apt.util.TemplateUtils;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.generic.ScopedInstance;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.ServiceLoader;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import java.util.stream.StreamSupport;

import static com.slimgears.util.stream.Streams.ofType;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.autovalue.annotations.AutoValuePrototype")
public class AutoValuePrototypeAnnotationProcessor extends AbstractAnnotationProcessor {
    private final static Logger log = LoggerFactory.getLogger(AutoValuePrototypeAnnotationProcessor.class);

    public static class Registrar {
        private final Collection<String> processedElements = new HashSet<>();
        private final static ScopedInstance<Registrar> instance = ScopedInstance.create(new Registrar());

        public Registrar() {
            log.debug("Registrar created");
        }

        public boolean needsProcessing(DeclaredType type) {
            boolean processed = processedElements.contains(type.asElement().toString());
            log.debug("Type {} is {} ({} processed types)", type.asElement().toString(), processed ? "processed" : "not processed", processedElements.size());
            return !processed;
        }

        public void processed(DeclaredType type) {
            processedElements.add(type.asElement().toString());
            log.debug("Adding processed type: {} ({} processed types)", type, processedElements.size());
        }

        public static Registrar current() {
            return instance.current();
        }

        public static ScopedInstance.Closeable scope() {
            return instance.scope(new Registrar());
        }
    }

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
        //generateInterfaceBuilder(declaredType);

        AutoValuePrototype annotation = type.getAnnotation(AutoValuePrototype.class);
        String targetName = annotation.value().isEmpty()
                ? type.getSimpleName().toString().replace("Prototype", "")
                : annotation.value();

        TypeInfo sourceClass = TypeInfo.of(type);
        TypeInfo targetClass = TypeInfo.of(sourceClass.packageName() + "." + targetName);
        Collection<PropertyInfo> properties = getProperties(declaredType);

        if (properties.stream().anyMatch(p -> ElementUtils.hasErrors(p.propertyType()))) {
            delayProcessing();
        }

        ensureBuildersForInterfaces(declaredType);
        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());
        List<PropertyInfo> keyProperties = properties.stream().filter(PropertyInfo::isKey).collect(Collectors.toList());
        if (keyProperties.size() > 1) {
            throw new IllegalArgumentException("Type " + type.getSimpleName() + " contains more than 1 key property");
        }
        PropertyInfo keyProperty = keyProperties.isEmpty() ? null : keyProperties.get(0);

        Context context = Context.builder()
                .sourceClass(sourceClass)
                .targetClass(targetClass)
                .properties(properties)
                .imports(importTracker)
                .keyProperty(keyProperty)
                .build();

        Extension extension = new CompositeExtension();

        try {
            TemplateEvaluator.forResource("auto-value.java.vm")
                    .variables(context)
                    .variable("context", context)
                    .variable("extensions", extension)
                    .variable("processor", TypeInfo.of(getClass()))
                    .variable("dollar", "$")
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

    private void ensureBuildersForInterfaces(DeclaredType declaredType) {
        ElementUtils
                .getHierarchy(declaredType)
                .filter(t -> ElementUtils.toTypeElement(declaredType).anyMatch(ElementUtils::isInterface))
                .filter(Registrar.current()::needsProcessing)
                .forEach(this::generateInterfaceBuilder);
    }

    private void generateInterfaceBuilder(DeclaredType type) {
        type = MoreTypes.asDeclared(type.asElement().asType());
        Registrar.current().processed(type);
        Collection<BuilderInfo> builders = getBuilders(type);

        log.debug("Generating builder for type: {} (declaration: {}{})", type, TypeInfo.of(type).name(), TypeInfo.of(type)
                .typeParams()
                .stream()
                .map(TypeParameterInfo::fullDeclaration)
                .collect(Collectors.joining(", ", "<", ">"))
        );

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
        return Stream
                .concat(
                        ElementUtils.getHierarchy(type)
                                .flatMap(valType -> ElementUtils.toTypeElement(valType)
                                    .map(TypeElement::getEnclosedElements)
                                    .flatMap(Collection::stream)
                                    .flatMap(ofType(TypeElement.class))
                                    .filter(isValidBuilder(valType))
                                    .map(te -> BuilderInfo.create(valType, MoreTypes.asDeclared(te.asType())))),
                        ElementUtils.toTypeElement(type)
                                .map(TypeElement::getInterfaces)
                                .flatMap(Collection::stream)
                                .map(TypeInfo::of)
                                .map(valType -> BuilderInfo.create(
                                        valType,
                                        valType.toBuilder()
                                                .name(valType.name() + "Builder")
                                                .typeParam("_B", TypeInfo.ofWildcard())
                                                .build())))
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
        Collection<ExecutableElement> elements = MoreElements
                .getLocalAndInheritedMethods(
                        MoreTypes.asTypeElement(type),
                        Environment.instance().types(),
                        Environment.instance().elements())
                .stream()
                .flatMap(this::isPropertyMethod)
                .collect(Collectors.toList());

        boolean hasPrefix = elements.stream().allMatch(this::propertyHasPrefix);

        return elements
                .stream()
                .map(ee -> PropertyInfo.create(type, ee, hasPrefix))
                .collect(Collectors.toList());
    }

    private Stream<ExecutableElement> isPropertyMethod(ExecutableElement executableElement) {
        return Stream
                .of(executableElement)
                .filter(ElementUtils::isAbstract)
                .filter(ElementUtils::isNotStatic)
                .filter(ElementUtils::isPublic)
                .filter(element -> element.getParameters().isEmpty());
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
