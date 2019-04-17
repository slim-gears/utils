/**
 *
 */
package com.slimgears.apt.util;

import com.google.common.base.Charsets;
import com.google.common.io.Resources;
import com.google.testing.compile.CompileTester;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubject;
import org.apache.commons.io.IOUtils;
import org.slf4j.event.Level;

import javax.annotation.processing.AbstractProcessor;
import javax.tools.JavaFileObject;
import javax.tools.SimpleJavaFileObject;
import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

import static com.google.common.truth.Truth.assert_;
import static com.google.testing.compile.JavaSourcesSubjectFactory.javaSources;

public class AnnotationProcessingTester {
    private final Collection<JavaFileObject> inputFiles = new ArrayList<>();
    private final Collection<AbstractProcessor> processors = new ArrayList<>();
    private final Collection<String> options = new ArrayList<>();
    private final Collection<Function<CompileTester.SuccessfulCompilationClause, CompileTester.SuccessfulCompilationClause>> assertions = new ArrayList<>();
    private Level verbosityLevel = Level.INFO;

    public static AnnotationProcessingTester create() {
        return new AnnotationProcessingTester();
    }

    public AnnotationProcessingTester verbosity(Level level) {
        verbosityLevel = level;
        return this;
    }

    public AnnotationProcessingTester options(String... options) {
        this.options.addAll(Arrays.asList(options));
        return this;
    }

    public AnnotationProcessingTester inputFiles(String... files) {
        inputFiles.addAll(fromInputResources("input", files));
        return this;
    }

    public AnnotationProcessingTester inputFiles(JavaFileObject... files) {
        inputFiles.addAll(Arrays.asList(files));
        return this;
    }

    public AnnotationProcessingTester expectedSources(String... files) {
        List<JavaFileObject> sources = fromResources("output", JavaFileObject.Kind.SOURCE, files);
        assertions.add(s -> s.and().generatesSources(sources.get(0), sources.stream().skip(1).toArray(JavaFileObject[]::new)));
        return this;
    }

    public AnnotationProcessingTester expectedFiles(String... files) {
        return expectedFiles(fromResources("output", JavaFileObject.Kind.OTHER, files));
    }

    public AnnotationProcessingTester expectedFile(String name, String... lines) {
        return expectedFiles(JavaFileObjects.forSourceLines(name, lines));
    }

    public AnnotationProcessingTester processedWith(AbstractProcessor... processors) {
        this.processors.addAll(Arrays.asList(processors));
        return this;
    }

    public AnnotationProcessingTester apply(Function<AnnotationProcessingTester, AnnotationProcessingTester> config) {
        return config.apply(this);
    }

    public void test() {
        options("-Averbosity=" + verbosityLevel);

        JavaSourcesSubject subject = assert_()
                .about(javaSources())
                .that(inputFiles)
                .withCompilerOptions(options);

        CompileTester.SuccessfulCompilationClause compilationClause = subject
                .processedWith(processors)
                .compilesWithoutError();

        assertions.stream()
                .reduce(Function::andThen)
                .orElse(c -> c)
                .apply(compilationClause);
    }

    private AnnotationProcessingTester expectedFiles(JavaFileObject... files) {
        return expectedFiles(Arrays.asList(files));
    }

    private AnnotationProcessingTester expectedFiles(List<JavaFileObject> files) {
        assertions.add(s -> s.and().generatesFiles(files.get(0), files.stream().skip(1).toArray(JavaFileObject[]::new)));
        return this;
    }

    private static List<JavaFileObject> fromInputResources(final String path, String[] files) {
        return Arrays.stream(files)
                .map(input -> JavaFileObjects.forResource(path + '/' + input))
                .collect(Collectors.toList());
    }

    private static List<JavaFileObject> fromResources(final String path, JavaFileObject.Kind kind, String[] files) {
        return Arrays.stream(files)
                .map(input -> forResource(kind, path + '/' + input))
//                .map(input -> JavaFileObjects.forResource(path + '/' + input))
                .collect(Collectors.toList());
    }

    private static JavaFileObject forResource(JavaFileObject.Kind kind, String filename) {
        return new ResourceJavaFileObject(filename, kind);
    }

    private static class ResourceJavaFileObject extends SimpleJavaFileObject {
        private final String content;

        ResourceJavaFileObject(String resourceName, Kind kind) {
            super(URI.create(Resources.getResource(resourceName).toString()), kind);
            try {
                content = FileUtils
                        .lineEndingsNormalizer()
                        .apply(Resources.toString(toUri().toURL(), Charsets.UTF_8).trim() + "\n");

            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        @Override
        public CharSequence getCharContent(boolean ignoreEncodingErrors) throws IOException {
            return content;
        }

        @Override
        public InputStream openInputStream() {
            return IOUtils.toInputStream(content, StandardCharsets.UTF_8);
        }
    }
}
