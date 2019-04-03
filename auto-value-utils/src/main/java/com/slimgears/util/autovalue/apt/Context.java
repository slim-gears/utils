package com.slimgears.util.autovalue.apt;

import com.google.auto.value.AutoValue;
import com.slimgears.apt.data.Environment;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.apt.util.TemplateEvaluator;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.apt.extensions.Annotator;
import com.slimgears.util.autovalue.apt.extensions.Extension;

import javax.annotation.Nullable;
import javax.lang.model.element.TypeElement;
import java.util.Collection;
import java.util.stream.Collectors;

@AutoValue
public abstract class Context {
    public abstract AutoValuePrototype meta();
    public abstract TypeInfo sourceClass();
    public abstract TypeElement sourceElement();
    public abstract TypeInfo targetClassDeclaration();
    public abstract TypeInfo targetClassWithParams();
    public abstract Extension extensions();
    public abstract Annotator annotators();
//    public abstract TypeInfo builderClassDeclaration();
//    public abstract TypeInfo builderClassWithParams();
    public abstract Collection<PropertyInfo> properties();
    public abstract ImportTracker imports();
    @Nullable public abstract PropertyInfo keyProperty();

    public Environment environment() {
        return Environment.instance();
    }

    public Context context() {
        return this;
    }

    public Collection<PropertyInfo> mandatoryProperties() {
        return properties()
                .stream()
                .filter(o -> !o.isOptional())
                .collect(Collectors.toList());
    }

    public TemplateEvaluator evaluatorForResource(String resourceName) {
        return TemplateEvaluator.forResource(resourceName).variables(this);
    }

    public String evaluateResource(String resourceName) {
        return evaluatorForResource(resourceName).evaluate();
    }

    public TypeInfo targetClass() {
        return targetClassWithParams().erasure();
    }

//    public TypeInfo builderClass() {
//        return builderClassWithParams().erasure();
//    }

    public static Builder builder() {
        return new AutoValue_Context.Builder();
    }

    public boolean hasKey() {
        return keyProperty() != null;
    }

    @AutoValue.Builder
    public interface Builder {
        Builder meta(AutoValuePrototype  meta);
        Builder sourceElement(TypeElement sourceElement);
        Builder sourceClass(TypeInfo cls);
        Builder targetClassDeclaration(TypeInfo cls);
        Builder targetClassWithParams(TypeInfo cls);
//        Builder builderClassDeclaration(TypeInfo cls);
//        Builder builderClassWithParams(TypeInfo cls);
        Builder properties(Collection<PropertyInfo> properties);
        Builder imports(ImportTracker imports);
        Builder keyProperty(PropertyInfo keyProperty);
        Builder extensions(Extension extension);
        Builder annotators(Annotator annotator);
        Context build();
    }
}
