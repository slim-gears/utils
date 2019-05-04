package com.slimgears.apt.util;

import com.google.escapevelocity.Template;
import com.slimgears.util.stream.Safe;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@SuppressWarnings("WeakerAccess")
public class TemplateEvaluator {
    private final static Logger log = LoggerFactory.getLogger(TemplateEvaluator.class);
    private final Map<String, Object> templateVariables = new HashMap<>();
    private final Collection<Function<String, String>> preProcessors = new ArrayList<>();
    private final Collection<Function<String, String>> postProcessors = new ArrayList<>();
    private final String resourceName;
    private final Template.ResourceOpener resourceOpener;

    private TemplateEvaluator(String resourceName, Template.ResourceOpener resourceOpener) {
        preProcess(TemplateUtils::preProcessWhitespace);
        postProcess(TemplateUtils::postProcessWhitespace);
        variable("dollar", "$");
        this.resourceName = resourceName;
        this.resourceOpener = applyPreprocessing(resourceOpener);
    }

    public static TemplateEvaluator forResource(String path) {
        return new TemplateEvaluator(path, combine(fromResources(), fromDirectory()));
    }

    public static TemplateEvaluator forFile(String path) {
        Path filePath = Paths.get(path);
        Path parent = filePath.getParent();
        return new TemplateEvaluator(filePath.getFileName().toString(), combine(fromDirectory(parent.toString()), fromResources()));
    }

    public TemplateEvaluator preProcess(Function<String, String> postProcessor) {
        preProcessors.add(postProcessor);
        return this;
    }

    public TemplateEvaluator postProcess(Function<String, String> postProcessor) {
        postProcessors.add(postProcessor);
        return this;
    }

    public <T> TemplateEvaluator variable(String name, T value) {
        templateVariables.put(name, value);
        return this;
    }

    public <T> TemplateEvaluator variables(T variables) {
        Set<String> objectMethods = Stream.of(Object.class.getMethods())
                .map(Method::getName)
                .collect(Collectors.toSet());

        Class cls = variables.getClass();
        Stream.of(cls.getMethods())
                .filter(m -> m.getParameterCount() == 0 &&
                        !objectMethods.contains(m.getName()) &&
                        Modifier.isPublic(m.getModifiers()) &&
                        !Modifier.isStatic(m.getModifiers()))
                .forEach(m -> {
                    try {
                        m.setAccessible(true);
                        variable(m.getName(), m.invoke(variables));
                    } catch (IllegalAccessException | InvocationTargetException ignored) {
                    }
                });

        return this;
    }

    public TemplateEvaluator apply(Function<TemplateEvaluator, TemplateEvaluator> config) {
        return config.apply(this);
    }

    public String evaluate() {
        try {
            Template template = Template.parseFrom(resourceName, resourceOpener);
            String source = template.evaluate(templateVariables);
            return postProcessors
                    .stream()
                    .reduce((a, b) -> str -> b.apply(a.apply(str)))
                    .orElse(str -> str)
                    .apply(source);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public void write(Consumer<String> writer) {
        writer.accept(evaluate());
    }

    private String preProcess(String templateCode) {
        String preprocessedTemplate = preProcessors
                .stream()
                .reduce((a, b) -> str -> b.apply(a.apply(str)))
                .orElse(str -> str)
                .apply(templateCode);
        log.trace("Template after preprocessing:");
        log.trace(preprocessedTemplate);
        return preprocessedTemplate;
    }

    private static Template.ResourceOpener fromResources() {
        return path -> Optional
                .ofNullable(TemplateEvaluator.class.getResourceAsStream("/" + path))
                .map(InputStreamReader::new)
                .orElse(null);
    }

    private static Template.ResourceOpener fromDirectory(String dir) {
        return path -> Optional.of(Paths.get(dir, path))
                .map(Safe.ofFunction(Files::newBufferedReader))
                .orElse(null);
    }
    private static Template.ResourceOpener fromDirectory() {
        return path -> Optional.of(Paths.get(path))
                .map(Safe.ofFunction(Files::newBufferedReader))
                .orElse(null);
    }

    private static Template.ResourceOpener combine(Template.ResourceOpener... openers) {
        return path -> Arrays
                .stream(openers)
                .map(Safe.ofFunction(o -> o.openResource(path)))
                .filter(Objects::nonNull)
                .findFirst()
                .orElse(null);
    }

    private Template.ResourceOpener applyPreprocessing(Template.ResourceOpener opener) {
        return path -> Optional
                .ofNullable(opener.openResource(path))
                .map(Safe.ofFunction(IOUtils::toString))
                .map(this::preProcess)
                .map(StringReader::new)
                .orElse(null);
    }
}
