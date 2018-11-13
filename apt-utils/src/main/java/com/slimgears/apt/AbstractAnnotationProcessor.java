package com.slimgears.apt;

import com.google.auto.common.MoreElements;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.util.LogUtils;
import com.slimgears.util.stream.Safe;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.RoundEnvironment;
import javax.annotation.processing.SupportedOptions;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.element.VariableElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.ofType;
import static com.slimgears.util.stream.Streams.takeWhile;

@SupportedOptions(LogUtils.verbosityOption)
public abstract class AbstractAnnotationProcessor extends AbstractProcessor {
    interface ElementSupplier<E extends Element> {
        E getElement(Elements elements);
    }

    static class PendingElementInfo {
        private final ElementSupplier<? extends Element> elementSupplier;
        private final TypeElement annotation;

        <E extends Element> E element() {
            //noinspection unchecked
            return (E)elementSupplier.getElement(Environment.instance().elements());
        }

        TypeElement annotation() {
            return this.annotation;
        }

        public  PendingElementInfo(Element element, TypeElement annotation) {
            this.annotation = annotation;
            this.elementSupplier = toElementSupplier(element);
        }

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
                        .filter(e -> ((Element) e).getKind() == elementKind && ((Element) e).getSimpleName().toString().equals(memberName))
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

    private final List<PendingElementInfo> pendingElements = new ArrayList<>();

    private static class DelayProcessingException extends RuntimeException {
    }

    protected final Logger log = LoggerFactory.getLogger(getClass());

    @Override
    public boolean process(Set<? extends TypeElement> annotations, RoundEnvironment roundEnv) {
        try (LogUtils.SelfClosable ignored = LogUtils.applyLogging(processingEnv);
             Safe.Closable envClosable = Environment.withEnvironment(processingEnv, roundEnv)) {
            processPendingElements();

            boolean res = annotations
                    .stream()
                    .map(a -> processAnnotation(a, roundEnv))
                    .reduce(Boolean::logicalOr)
                    .orElse(false);

            if (isProcessingOver(roundEnv, annotations)) {
                onComplete();
            }
            return res;
        }
        catch (Exception e) {
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

    protected void onComplete() {

    }

    private void processPendingElements() {
        PendingElementInfo[] pendingElements = this.pendingElements.toArray(new PendingElementInfo[0]);
        this.pendingElements.clear();
        Arrays.stream(pendingElements).forEach(pe -> processElement(pe.annotation(), pe.element()));
    }

    private boolean processElement(TypeElement annotationType, Element element) {
        try {
            return Stream
                    .of(
                            ofType(TypeElement.class, Stream.of(element)).map(el -> processType(annotationType, el)),
                            ofType(ExecutableElement.class, Stream.of(element)).map(el -> processMethod(annotationType, el)),
                            ofType(VariableElement.class, Stream.of(element)).map(el -> processField(annotationType, el)))
                    .flatMap(s -> s)
                    .findAny()
                    .orElse(true);
        } catch (DelayProcessingException ignored) {
            pendingElements.add(new PendingElementInfo(element, annotationType));
            return true;
        }
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

    protected void delayProcessing() {
        throw new DelayProcessingException();
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
