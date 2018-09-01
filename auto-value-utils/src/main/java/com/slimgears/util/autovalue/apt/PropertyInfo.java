package com.slimgears.util.autovalue.apt;

import com.google.auto.value.AutoValue;
import com.slimgears.apt.data.HasAnnotations;
import com.slimgears.apt.data.HasName;
import com.slimgears.apt.data.HasType;
import com.slimgears.apt.data.InfoBuilder;

import javax.lang.model.element.ExecutableElement;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@AutoValue
public abstract class PropertyInfo implements HasName, HasType, HasAnnotations {
    private static final Pattern namePattern = Pattern.compile("^(get)|(is)(?<name>[A-Z]\\w*)$");

    public static Builder builder() {
        return new AutoValue_PropertyInfo.Builder();
    }

    public static PropertyInfo of(ExecutableElement element) {
        return builder()
                .name(propertyName(element.getSimpleName().toString()))
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
    }
}
