package com.slimgears.util.guice.apt;

import com.google.auto.service.AutoService;
import com.slimgears.apt.AbstractAnnotationProcessor;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.FileUtils;
import com.slimgears.util.guice.ConfigBinding;
import com.slimgears.util.guice.ConfigBindingModule;

import javax.annotation.processing.Processor;
import javax.annotation.processing.SupportedAnnotationTypes;
import javax.lang.model.element.TypeElement;
import javax.tools.StandardLocation;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

@AutoService(Processor.class)
@SupportedAnnotationTypes("com.slimgears.util.guice.ConfigBinding")
public class ConfigBindingAnnotationProcessor extends AbstractAnnotationProcessor {
    private final Map<TypeInfo, String> configBindings = new HashMap<>();

    @Override
    protected boolean processType(TypeElement annotationType, TypeElement typeElement) {
        ConfigBinding binding = typeElement.getAnnotation(ConfigBinding.class);
        configBindings.put(TypeInfo.of(typeElement), binding.value());
        return true;
    }

    @Override
    protected void onComplete() {
        String content = configBindings.entrySet()
                .stream()
                .map(entry -> entry.getKey() + " = " + entry.getValue())
                .collect(Collectors.joining(System.lineSeparator()));

        FileUtils.fileWriter(StandardLocation.CLASS_OUTPUT, ConfigBindingModule.resourcePath)
                .accept(content);
    }
}
