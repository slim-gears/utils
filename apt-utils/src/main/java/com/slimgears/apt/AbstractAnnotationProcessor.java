package com.slimgears.apt;

import com.google.auto.common.MoreElements;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.util.ElementUtils;
import com.slimgears.apt.util.LogUtils;
import com.slimgears.util.stream.Safe;
import com.slimgears.util.stream.Streams;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.*;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.lang.annotation.AnnotationTypeMismatchException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;
import static com.slimgears.util.stream.Streams.takeWhile;

@SuppressWarnings("WeakerAccess")
@SupportedOptions({
        LogUtils.verbosityOption,
        Environment.configOptionName,
        Environment.excludedTypesOptionName,
        Environment.includeTypesOptionName})
public abstract class AbstractAnnotationProcessor extends AbstractProcessor {
    interface ElementSupplier<E extends Element> {
        E getElement(Elements elements);
    }

    static class PendingElementInfo {
        private final ElementSupplier<? extends Element> elementSupplier;
        private final TypeElement annotation;

        @SuppressWarnings("unchecked")
        <E extends Element> E element() {
            return (E)elementSupplier.getElement(Environment.instance().elements());
        }

        TypeElement annotation() {
            return this.annotation;
        }

        PendingElementInfo(Element element, TypeElement annotation) {
            this.annotation = annotation;
            this.elementSupplier = toElementSupplier(element);
        }

        @SuppressWarnings("UnstableApiUsage")
        private ElementSupplier<? extends Element> toElementSupplier(Element element) {
            if (element instanceof TypeElement) {
                return toTypeElementSupplier(MoreElements.asType(element));
            } else if (element.getEnclosingElement() instanceof TypeElement) {
                ElementSupplier<TypeElement> typeSupplier = toTypeElementSupplier(MoreElements.asType(element.getEnclosingElement()));
                String memberName = element.getSimpleName().toString();
                ElementKind elementKind = element.getKind();
                return elements -> typeSupplier
                        .getElement(elements)
                        .getEnclosedElements()
                        .stream()
                        .filter(e -> e.getKind() == elementKind && e.getSimpleName().toString().equals(memberName))
                        .findAny()
                        .orElseThrow(() -> new IllegalStateException("Member " + memberName + " not found"));
            }
            return elements -> element;
        }

        private ElementSupplier<TypeElement> toTypeElementSupplier(TypeElement element) {
            String typeName = element.getQualifiedName().toString();
            return elements -> elements.getTypeElement(typeName);
        }
    }

    private final Map<String, PendingElementInfo> pendingElements = new HashMap<>();

    private static class DelayProcessingException extends RuntimeException {
        DelayProcessingException(String reason) {
            super(reason);
        }
    }

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try (LogUtils.SelfClosable ignored1 = LogUtils.applyLogging(processingEnv);
             Safe.Closeable ignored2 = Environment.withEnvironment(processingEnv, roundEnv)) {
            processPendingElements();

            boolean res = annotations
                    .stream()
                    .map(a -> processAnnotation(a, roundEnv))
                    .reduce(Boolean::logicalOr)
                    .orElse(false);

            onRoundFinished();
            if (isProcessingOver(roundEnv, annotations)) {
                onComplete();
            }
            return res;
        }
        catch (Exception e) {
            System.err.println("Error!!!: " + e);
            log.error("Error: ", e);
            processingEnv.getMessager().printMessage(Diagnostic.Kind.ERROR, e.getMessage());
            return true;
        }
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return processingEnv.getSourceVersion();
    }

    protected boolean isProcessingOver(RoundEnvironment roundEnvironment, Set<? extends TypeElement> annotations) {
        return roundEnvironment.processingOver();
    }

    protected void onRoundFinished() {

    }

    protected void onComplete() {

    }

    private void processPendingElements() {
        Iterable<PendingElementInfo> pendingElements = ImmutableList.copyOf(this.pendingElements.values());
        pendingElements.forEach(pe -> processElement(pe.annotation(), pe.element()));
    }

    private boolean processElement(TypeElement annotationType, Element element) {
        try {
            boolean res = Stream
                    .of(
                            ofType(TypeElement.class, Stream.of(element)).map(el -> processType(annotationType, el)),
                            ofType(ExecutableElement.class, Stream.of(element)).map(el -> processMethod(annotationType, el)),
                            ofType(VariableElement.class, Stream.of(element)).map(el -> processField(annotationType, el)))
                    .flatMap(s -> s)
                    .findAny()
                    .orElse(true);
            pendingElements.remove(ElementUtils.fullName(element));
        } catch (AnnotationTypeMismatchException | ClassCastException e) {
            addPendingElement(element, annotationType);
        } catch (DelayProcessingException e) {
            log.debug("Processing of element {} with annotation {} delayed until next round: {}",
                    element.getSimpleName(),
                    annotationType.getSimpleName(),
                    e.getMessage());
            addPendingElement(element, annotationType);
        }
        return true;
    }

    private void addPendingElement(Element element, TypeElement annotationType) {
        pendingElements.computeIfAbsent(ElementUtils.fullName(element), en -> new PendingElementInfo(element, annotationType));
    }

    protected boolean processAnnotation(TypeElement annotationType, RoundEnvironment roundEnv) {
        return roundEnv.getElementsAnnotatedWith(annotationType)
                .stream()
                .map(element -> processElement(annotationType, element))
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

    protected void delayProcessing(String reason) {
        throw new DelayProcessingException(reason);
    }

    @SuppressWarnings("unused")
    protected void delayProcessing(Iterable<TypeMirror> types) {
        delayProcessing("Types: [" + Streams
                .fromIterable(types)
                .map(Object::toString)
                .collect(Collectors.joining(", "))
                + "] are not resolved");
    }

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
