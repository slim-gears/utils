package com.slimgears.apt.data;

import com.google.auto.common.AnnotationMirrors;
import com.google.auto.common.MoreElements;
import com.google.auto.common.MoreTypes;
import com.google.auto.value.AutoOneOf;
import com.google.auto.value.AutoValue;
import com.google.common.base.MoreObjects;
import com.google.common.collect.ImmutableList;
import com.slimgears.apt.util.ImportTracker;
import com.slimgears.util.stream.Optionals;

import javax.lang.model.element.AnnotationMirror;
import javax.lang.model.element.AnnotationValue;
import javax.lang.model.type.TypeMirror;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@AutoValue
public abstract class AnnotationValueInfo implements HasName, HasType {
    public enum Kind {
        Primitive,
        Array,
        Type,
        Annotation
    }

    @AutoOneOf(Kind.class)
    public static abstract class Value {
        public abstract Kind kind();
        public abstract Object primitive();
        public abstract AnnotationInfo annotation();
        public abstract TypeInfo type();
        public abstract ImmutableList<Value> array();

        public String asString() {
            if (kind() == Kind.Annotation) {
                return annotation().asString();
            } else if (kind() == Kind.Type) {
                return ImportTracker.useType(TypeInfo.of(type().erasureName())) + ".class";
            } else if (kind() == Kind.Array) {
                ImmutableList<Value> items = array();
                return items.size() != 1
                        ? items.stream().map(Value::asString).collect(Collectors.joining(", ", "{", "}"))
                        : items.get(0).asString();
            } else {
                Object primitive = primitive();
                if (primitive instanceof String) {
                    return '"' + primitive.toString() + '"';
                } else {
                    return primitive.toString();
                }
            }
        }

        public static Value ofType(TypeInfo typeInfo) {
            return AutoOneOf_AnnotationValueInfo_Value.type(typeInfo);
        }

        public static Value ofPrimitive(Object primitive) {
            return AutoOneOf_AnnotationValueInfo_Value.primitive(primitive);
        }

        public static Value ofArray(Value... array) {
            return AutoOneOf_AnnotationValueInfo_Value.array(ImmutableList.copyOf(array));
        }

        public static Value ofAnnotation(AnnotationInfo annotation) {
            return AutoOneOf_AnnotationValueInfo_Value.annotation(annotation);
        }
    }

    public abstract Value value();

    public boolean isPrimitive() {
        return value().kind() == Kind.Primitive;
    }

    public boolean isAnnotation() {
        return value().kind() == Kind.Annotation;
    }

    public boolean isType() {
        return value().kind() == Kind.Type;
    }

    public boolean isArray() {
        return value().kind() == Kind.Array;
    }

    public static Builder builder() {
        return new AutoValue_AnnotationValueInfo.Builder();
    }

    public static AnnotationValueInfo of(String name, Value value) {
        return builder().name(name).value(value).build();
    }

    public static AnnotationValueInfo of(String name, AnnotationValue value) {
        Optional<Object> val = Optional.of(value.getValue());
        return Optionals
                .or(
                        () -> val.map(v -> AnnotationValueInfo.ofPrimitive(name, v)),
                        () -> val.flatMap(Optionals.ofType(TypeMirror.class)).map(v -> AnnotationValueInfo.ofType(name, v)),
                        () -> val.flatMap(Optionals.ofType(AnnotationMirror.class)).map(v -> AnnotationValueInfo.ofAnnotation(name, v)))
                .orElse(null);
    }

    public static AnnotationValueInfo ofType(String name, TypeInfo type) {
        return builder().name(name).type(Class.class).value(Value.ofType(type)).build();
    }

    public static AnnotationValueInfo ofType(String name, Class cls) {
        return ofType(name, TypeInfo.of(cls));
    }

    public static AnnotationValueInfo ofType(String name, TypeMirror type) {
        return builder().name(name).type(Class.class).value(Value.ofType(TypeInfo.of(type))).build();
    }

    public static AnnotationValueInfo ofPrimitive(String name, Object value) {
        return builder().name(name).type(value.getClass()).value(value).build();
    }

    public static AnnotationValueInfo ofAnnotation(String name, AnnotationInfo annotation) {
        return builder()
                .name(name)
                .type(annotation.type())
                .value(annotation)
                .build();
    }

    public static AnnotationValueInfo ofArray(String name, TypeInfo type, Value... values) {
        return builder()
                .name(name)
                .type(type)
                .value(Value.ofArray(values))
                .build();
    }

    public static AnnotationValueInfo ofArray(String name, Class cls, Value... values) {
        return builder()
                .name(name)
                .type(TypeInfo.of(cls))
                .value(Value.ofArray(values))
                .build();
    }

    @SafeVarargs
    public static <T> AnnotationValueInfo ofArray(String name, Class cls, T... values) {
        return builder()
                .name(name)
                .type(TypeInfo.of(cls))
                .value(Value.ofArray(Stream.of(values).map(Value::ofPrimitive).toArray(Value[]::new)))
                .build();
    }

    public static AnnotationValueInfo ofAnnotation(String name, AnnotationMirror annotation) {
        return builder()
                .name(name)
                .type(annotation.getClass())
                .value(AnnotationInfo.of(annotation))
                .build();
    }

    @AutoValue.Builder
    public interface Builder extends
            InfoBuilder<AnnotationValueInfo>,
            HasName.Builder<Builder>,
            HasType.Builder<Builder> {
        Builder value(Value value);

        default Builder value(AnnotationInfo value) {
            return value(Value.ofAnnotation(value));
        }
        default Builder value(Object primitive) {
            return value(Value.ofPrimitive(primitive));
        }
    }
}
