package com.slimgears.util.guice.apt;

import com.google.auto.service.AutoService;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.FileUtils;
import com.slimgears.util.guice.AutoBinding;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.guice.AutoBinding")
public class AutoBindingAnnotationProcessor extends AbstractAnnotationProcessor {
    private final Map<TypeInfo, List<TypeElement>> autoBindTypes = new HashMap<>();

    @Override
    protected boolean processType(TypeElement annotationType, TypeElement typeElement) {
        AutoBinding annotation = typeElement.getAnnotation(AutoBinding.class);
        TypeInfo moduleType = ElementUtils.typeFromAnnotation(annotation, AutoBinding::module);
        autoBindTypes.computeIfAbsent(moduleType, k -> new ArrayList<>())
                .add(typeElement);
        return true;
    }

    @Override
    protected void onComplete() {
        autoBindTypes.forEach(this::writeModule);
    }

    private void writeModule(TypeInfo moduleType, List<TypeElement> types) {
        String content = types
                .stream()
                .map(type -> TypeInfo.of(type).name())
                .collect(Collectors.joining(System.lineSeparator()));

        FileUtils.fileWriter(StandardLocation.CLASS_OUTPUT, "META-INF/autobinding/" + moduleType.name())
                .accept(content);
    }
}
