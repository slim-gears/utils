package com.slimgears.apt.util;

import com.google.escapevelocity.Template;
import org.apache.commons.io.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Supplier;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class TemplateEvaluator {
    private final static Logger log = LoggerFactory.getLogger(TemplateEvaluator.class);
    private final Map<String, Object> templateVariables = new HashMap<>();
    private final Collection<Function<String, String>> preProcessors = new ArrayList<>();
    private final Collection<Function<String, String>> postProcessors = new ArrayList<>();
    private final Supplier<Reader> reader;

    private TemplateEvaluator(Supplier<Reader> reader) {
        this.reader = reader;
    }

    public static TemplateEvaluator forReader(Supplier<Reader> reader) {
        return new TemplateEvaluator(reader);
    }

    public static TemplateEvaluator forStream(Supplier<InputStream> stream) {
        return forReader(() -> new InputStreamReader(stream.get()));
    }

    public static TemplateEvaluator forResource(String path) {
        return forStream(() -> TemplateEvaluator.class.getResourceAsStream(path));
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
                    } catch (IllegalAccessException | InvocationTargetException e) {
                    }
                });

        return this;
    }

    public TemplateEvaluator apply(Function<TemplateEvaluator, TemplateEvaluator> config) {
        return config.apply(this);
    }

    public String evaluate() {
        try {
            String templateCode = IOUtils.toString(reader.get());
            templateCode = preProcessors
                    .stream()
                    .reduce((a, b) -> str -> b.apply(a.apply(str)))
                    .orElse(str -> str)
                    .apply(templateCode);

            log.trace("Preprocessed template:");
            LogUtils.dumpContent(templateCode);

            StringReader strReader = new StringReader(templateCode);
            Template template = Template.parseFrom(strReader);
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
}
