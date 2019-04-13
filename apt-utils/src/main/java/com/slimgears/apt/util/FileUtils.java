package com.slimgears.apt.util;

import com.slimgears.apt.data.Environment;
import org.apache.commons.io.FilenameUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.Filer;
import javax.tools.FileObject;
import javax.tools.StandardLocation;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.function.Consumer;
import java.util.function.Function;

public class FileUtils {
    private final static Logger log = LoggerFactory.getLogger(FileUtils.class);

    public static Function<String, String> lineEndingsNormalizer() {
        return lineEndingsNormalizer(System.lineSeparator());
    }

    public static Function<String, String> lineEndingsNormalizer(String lineSeparator) {
        return content -> content
                .replaceAll("\\r\\n", "\n")
                .replaceAll("\\r", "\n")
                .replaceAll("\\n", lineSeparator);
    }

    public static Consumer<String> fileWriter(String filename) {
        return fileWriter(StandardLocation.SOURCE_OUTPUT, filename);
    }

    public static Consumer<String> fileWriter(StandardLocation location, String filename) {
        return content -> {
            log.info("Writing file: {}", filename);

            content = lineEndingsNormalizer().apply(content.trim() + "\n");
            LogUtils.dumpContent(content);

            Filer filer = Environment.instance().processingEnvironment().getFiler();
            try {
                FileObject fileObject = filer.createResource(location, "", filename);
                try (OutputStream stream = fileObject.openOutputStream();
                     PrintWriter printWriter = new PrintWriter(stream)) {
                    printWriter.print(content);
                }

                Environment.instance()
                        .fileListener()
                        .onFileWrite(FilenameUtils.getBaseName(filename), content);

            } catch (IOException e) {
                log.warn("Could not write file: {}", filename, e);
            }
        };
    }
}
