package com.slimgears.apt;

import com.slimgears.apt.data.Environment;
import com.slimgears.apt.util.LogUtils;
import com.slimgears.util.stream.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;
import static com.slimgears.util.stream.Streams.takeWhile;

@SupportedOptions(LogUtils.verbosityOption)
public abstract class AbstractAnnotationProcessor extends AbstractProcessor {
    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try (LogUtils.SelfClosable ignored = LogUtils.applyLogging(processingEnv);
             Safe.Closable envClosable = Environment.withEnvironment(processingEnv, roundEnv)) {
            boolean res = annotations
                    .stream()
                    .map(a -> processAnnotation(a, roundEnv))
                    .reduce(Boolean::logicalOr)
                    .orElse(false);

            if (annotations.isEmpty()) {
                onComplete();
            }
            return res;
        } catch (AnnotationTypeMismatchException e) {
            return false;
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    protected void onComplete() {

    }

    protected boolean processAnnotation(TypeElement annotationType, RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(annotationType)
                .stream()
                .flatMap(e -> Stream
                        .of(
                                ofType(TypeElement.class, Stream.of(e)).map(_e -> processType(annotationType, _e)),
                                ofType(ExecutableElement.class, Stream.of(e)).map(_e -> processMethod(annotationType, _e)),
                                ofType(VariableElement.class, Stream.of(e)).map(_e -> processField(annotationType, _e)))
                        .flatMap(s -> s))
                .reduce(Boolean::logicalOr)
                .orElse(false);
    }

    protected boolean processAnnotation(Class annotationType, RoundEnvironment roundEnv) {
        return processAnnotation(
                processingEnv.getElementUtils().getTypeElement(annotationType.getCanonicalName()),
                roundEnv);
    }

    protected boolean processType(TypeElement annotationType, TypeElement typeElement) {
        return false;
    }

    protected boolean processMethod(TypeElement annotationType, ExecutableElement methodElement) { return false; }
    protected boolean processField(TypeElement annotationType, VariableElement variableElement) { return false; }

    @Override
    public Set<String> getSupportedOptions() {
        return Stream
                .concat(
                        takeWhile(Stream.iterate((Class)getClass(), Class::getSuperclass), cls -> cls != Object.class)
                                .flatMap(cls -> Optional
                                        .ofNullable(((Class<?>)cls).getAnnotation(SupportedOptions.class))
                                        .map(SupportedOptions::value)
                                        .map(Arrays::stream)
                                        .orElseGet(Stream::empty)),
                        getAdditionalSupportedOptions())
                .collect(Collectors.toSet());
    }

    protected Stream<String> getAdditionalSupportedOptions() {
        return Stream.empty();
    }
}
