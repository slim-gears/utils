package com.slimgears.util.autovalue.apt.extensions;

import com.google.auto.common.MoreTypes;
import com.google.auto.service.AutoService;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.JavaUtils;
import com.slimgears.apt.util.TemplateEvaluator;
import com.slimgears.apt.util.TemplateUtils;
import com.slimgears.util.autovalue.annotations.UseBuilderExtension;
import com.slimgears.util.autovalue.apt.BuilderInfo;
import com.slimgears.util.autovalue.apt.Context;
import com.slimgears.util.autovalue.apt.PropertyInfo;
import com.slimgears.util.autovalue.apt.PropertyUtils;
import com.slimgears.util.autovalue.apt.Registrar;
import com.slimgears.util.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.lang.model.element.ElementKind;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.TypeParameterElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import java.util.Collection;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;

@AutoService(Extension.class)
@SupportedAnnotations(UseBuilderExtension.class)
public class BuilderExtension implements Extension {
    private final static Logger log = LoggerFactory.getLogger(BuilderExtension.class);

    @Override
    public String generateClassBody(Context context) {
        ensureBuildersForInterfaces(context.sourceElement());
        return context.evaluateResource("builder-body.java.vm");
    }

    private static void ensureBuildersForInterfaces(TypeElement typeElement) {
        DeclaredType declaredType = MoreTypes.asDeclared(typeElement.asType());
        ElementUtils
                .getHierarchy(declaredType)
                .filter(BuilderExtension::isNotJavaType)
                .filter(BuilderExtension::hasProperties)
                .filter(t -> ElementUtils.toTypeElement(declaredType).anyMatch(ElementUtils::isInterface))
                .filter(Registrar.current()::needsProcessing)
                .forEach(BuilderExtension::generateInterfaceBuilder);
    }

    private static void generateInterfaceBuilder(DeclaredType type) {
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
        Collection<PropertyInfo> properties = PropertyUtils.getProperties(type);
        ImportTracker importTracker = ImportTracker.create("java.lang", targetClass.packageName());

        TemplateEvaluator.forResource("auto-value-builder.java.vm")
                .variable("utils", new TemplateUtils())
                .variable("builders", builders)
                .variable("properties", properties)
                .variable("processor", TypeInfo.of(BuilderExtension.class))
                .variable("sourceClass", sourceClass)
                .variable("targetClass", targetClass)
                .variable("imports", importTracker)
                .apply(JavaUtils.imports(importTracker))
                .write(JavaUtils.fileWriter(Environment.instance().processingEnvironment(), targetClass));
    }

    private static Collection<BuilderInfo> getBuilders(DeclaredType type) {
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
                                .flatMap(Streams.ofType(DeclaredType.class))
                                .filter(BuilderExtension::isNotJavaType)
                                .filter(BuilderExtension::hasProperties)
                                .map(TypeInfo::of)
                                .map(valType -> BuilderInfo.create(
                                        valType,
                                        valType.toBuilder()
                                                .name(valType.name() + "Builder")
                                                .typeParam("_B", TypeInfo.ofWildcard())
                                                .build())))
                .collect(Collectors.toList());
    }

    private static boolean isNotJavaType(DeclaredType type) {
        return !type.toString().startsWith("java.");
    }

    private static boolean hasProperties(DeclaredType type) {
        return PropertyUtils.hasProperties(type);
    }

    private static Predicate<TypeElement> isValidBuilder(DeclaredType type) {
        return te -> te.getKind() == ElementKind.INTERFACE &&
                isValidBuilderName(te) &&
                hasValidTypeParams(type, te);
    }

    private static boolean isValidBuilderName(TypeElement typeElement) {
        return "Builder".equals(typeElement.getSimpleName().toString());
    }

    private static boolean hasValidTypeParams(DeclaredType valType, TypeElement typeElement) {
        int paramSize = typeElement.getTypeParameters().size();
        return paramSize == valType.getTypeArguments().size() + 1 &&
                hasValidBuilderBounds(typeElement, typeElement.getTypeParameters().get(paramSize - 1));
    }

    private static boolean hasValidBuilderBounds(TypeElement builderType, TypeParameterElement typeParamElement) {
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

}
