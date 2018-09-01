/**
 *
 */
package com.slimgears.util.generic;

import com.google.auto.service.AutoService;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.MethodInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.*;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Types;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.generic.AutoGeneric")
public class AutoGenericAnnotationProcessor extends AbstractAnnotationProcessor {
    @Override
    protected boolean processType(TypeElement annotationType, TypeElement type) {
        AutoGeneric annotation = type.getAnnotation(AutoGeneric.class);
        NameTemplateUtils.validateNameTemplate(annotation.className(), type);
        Stream.of(annotation.implementations())
                .forEach(imp -> generateClass(type, annotation, imp));
        return true;
    }

    private void generateClass(TypeElement sourceType, AutoGeneric classAnnotation, AutoGeneric.WithParams implementationAnnotation) {
        TypeMirror[] typeArgMirrors = ElementUtils.typeMirrorsFromAnnotation(implementationAnnotation, AutoGeneric.WithParams::value);
        TypeInfo[] typeArgs = Stream.of(typeArgMirrors).map(TypeInfo::of).toArray(TypeInfo[]::new);

        Preconditions.checkArgument(
                typeArgs.length == sourceType.getTypeParameters().size(),
                "@AutoGeneric.WithParams parameters number mismatch.");

        String nameTemplate = classAnnotation.className();
        TypeInfo sourceClass = TypeInfo.of(sourceType);
        Types types = Environment.instance().types();
        DeclaredType superTypeMirror = types.getDeclaredType(sourceType, typeArgMirrors);
        MethodInfo[] constructors = sourceType
                .getEnclosedElements()
                .stream()
                .filter(ElementUtils.ofKind(ElementKind.CONSTRUCTOR))
                .flatMap(ofType(ExecutableElement.class))
                .map(ee -> MethodInfo.create(ee, superTypeMirror))
                .toArray(MethodInfo[]::new);

        TypeInfo superClass = TypeInfo.builder()
                .name(sourceType.getQualifiedName().toString())
                .typeParams(typeArgs)
                .build();

        String packageName = sourceClass.packageName();
        String targetTypeName = NameTemplateUtils.getTypeName(nameTemplate, sourceType, typeArgs);
        TypeInfo targetClass = TypeInfo.of(packageName + "." + targetTypeName);
        JavaUtils javaUtils = new JavaUtils();

        ImmutableMap<String, TypeInfo> typeParams = IntStream
                .range(0, typeArgs.length)
                .boxed()
                .collect(ImmutableMap.toImmutableMap(
                        i -> sourceClass.typeParams().get(i).typeName(),
                        i -> typeArgs[i]));

        ImmutableList<MappedConstructorInfo> mappedConstructors = Stream.of(constructors)
                .map(c -> MappedConstructorInfo.builder()
                        .superConstructor(c)
                        .classParams(typeParams)
                        .params(c.params().stream().filter(p -> !p.hasAnnotation(AutoGeneric.ClassParam.class)))
                        .build())
                .collect(ImmutableList.toImmutableList());

        ImportTracker importTracker = ImportTracker.create(packageName, "java.lang");

        TemplateEvaluator
                .forResource("auto-generic.java.vm")
                .variable("packageName", packageName)
                .variable("processor", getClass().getName())
                .variable("imports", importTracker)
                .variable("sourceClass", sourceClass)
                .variable("targetClass", targetClass)
                .variable("superClass", superClass)
                .variable("mappedConstructors", mappedConstructors)
                .variable("typeParams", typeParams)
                .variable("classParamAnnotation", TypeInfo.of(AutoGeneric.ClassParam.class))
                .variable("utils", javaUtils)
                .apply(JavaUtils.imports(importTracker))
                .write(JavaUtils.fileWriter(processingEnv, targetClass));
    }
}
