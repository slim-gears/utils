package com.slimgears.util.autovalue.apt;

import com.google.auto.value.AutoValue;
import com.slimgears.apt.data.HasAnnotations;
import com.slimgears.apt.data.HasName;
import com.slimgears.apt.data.HasType;
import com.slimgears.apt.data.InfoBuilder;
import com.slimgears.util.stream.Optionals;
import com.slimgears.util.stream.Streams;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.element.TypeElement;
import javax.lang.model.type.DeclaredType;
import java.util.Optional;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
public abstract class PropertyInfo implements HasName, HasType, HasAnnotations {
    private static final Pattern namePattern = Pattern.compile("^(get)|(is)(?<name>[A-Z]\\w*)$");

    public abstract ExecutableElement element();
    public TypeElement typeElement() {
        return Optional.of(element())
                .map(ExecutableElement::getReturnType)
                .flatMap(Optionals.ofType(DeclaredType.class))
                .map(DeclaredType::asElement)
                .flatMap(Optionals.ofType(TypeElement.class))
                .orElse(null);
    }

    public boolean hasBuilder() {
        return typeElement()
                .getEnclosedElements()
                .stream()
                .flatMap(Streams.ofType(TypeElement.class))
                .filter(t -> "Builder".equals(t.getSimpleName().toString()))
                .anyMatch(t -> typeElement().getTypeParameters().size() == t.getTypeParameters().size());
    }

    public static Builder builder() {
        return new AutoValue_PropertyInfo.Builder();
    }

    public static PropertyInfo of(ExecutableElement element) {
        return builder()
                .name(propertyName(element.getSimpleName().toString()))
                .element(element)
                .type(element.getReturnType())
                .annotationsFromElement(element)
                .build();
    }

    private static String propertyName(String name) {
        Matcher matcher = namePattern.matcher(name);
        return (matcher.matches())
                ? matcher.group("name")
                : name;
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<PropertyInfo>,
            HasName.Builder<Builder>,
            HasType.Builder<Builder>,
            HasAnnotations.Builder<Builder> {
        Builder element(ExecutableElement element);
    }
}
