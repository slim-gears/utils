/**
 *
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.ProcessingEnvironment;
import javax.tools.JavaFileObject;
import java.io.IOException;
import java.io.Writer;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

public class JavaUtils extends TemplateUtils {
    private final static Logger log = LoggerFactory.getLogger(JavaUtils.class);
    public static Function<TemplateEvaluator, TemplateEvaluator> imports(ImportTracker importTracker) {
        return evaluator -> evaluator
                .variable("imports", importTracker)
                .postProcess(TemplateUtils.postProcessImports(importTracker))
                .postProcess(code -> addImports(importTracker, code, imp -> "import " + imp + ";"));
    }

    public static Consumer<String> fileWriter(ProcessingEnvironment environment, TypeInfo targetClass) {
        return code -> writeJavaFile(environment, targetClass, code);
    }

    private static String addImports(ImportTracker importTracker, String code, Function<String, String> importSubstitutor) {
        String importsStr = Stream.of(importTracker.imports())
                .map(importSubstitutor)
                .collect(Collectors.joining("\n"));
        return code.replace(importTracker.toString(), importsStr);
    }

    private static void writeJavaFile(ProcessingEnvironment environment, TypeInfo targetClass, String code) {
        try {
            log.debug("Writing class: {}", targetClass.name());
            LogUtils.dumpContent(code);
            JavaFileObject sourceFile = environment.getFiler().createSourceFile(targetClass.name());
            try (Writer writer = sourceFile.openWriter()) {
                writer.write(code);
            }
        } catch (IOException e) {
            log.error(code, e);
            throw new RuntimeException(e);
        }
    }
}
