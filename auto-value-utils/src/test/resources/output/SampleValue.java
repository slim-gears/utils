package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.MetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.StringPropertyExpression;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;
import javax.annotation.Nullable;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleValue implements SampleValuePrototype {

    public static final Expressions<SampleValue> $ = new Expressions<>();
    public static Expressions<SampleValue> $() {
        return $;
    }

    public static class Expressions<__S> {
        private final ObjectExpression<__S, SampleValue> self = ObjectExpression.arg();
        private final Meta meta = new Meta() ;

        public final NumericPropertyExpression<__S, SampleValue, Builder, Integer> intValue = PropertyExpression.ofNumeric(self, meta.intValue);
        public final NumericPropertyExpression<__S, SampleValue, Builder, Double> doubleValue = PropertyExpression.ofNumeric(self, meta.doubleValue);
        public final StringPropertyExpression<__S, SampleValue, Builder> strValue = PropertyExpression.ofString(self, meta.strValue);
        public final BooleanPropertyExpression<__S, SampleValue, Builder> foo = PropertyExpression.ofBoolean(self, meta.foo);
    }

    public static class ReferencePropertyExpression<__S, __T, __B> extends Expressions<__S> implements PropertyExpression<__S, __T, __B, SampleValue> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, SampleValue> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleValue> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B> ReferencePropertyExpression<__S, __T, __B> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleValue> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, SampleValue> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

    public MetaClass<SampleValue, SampleValue.Builder> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClass<SampleValue, SampleValue.Builder> {
        private final TypeToken<SampleValue> objectClass = new TypeToken<SampleValue>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleValue, Builder, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleValue, Builder, Integer> intValue = PropertyMeta.create(objectClass, "intValue", new TypeToken<Integer>(){}, SampleValue::intValue, Builder::intValue);
        public final PropertyMeta<SampleValue, Builder, Double> doubleValue = PropertyMeta.create(objectClass, "doubleValue", new TypeToken<Double>(){}, SampleValue::doubleValue, Builder::doubleValue);
        public final PropertyMeta<SampleValue, Builder, String> strValue = PropertyMeta.create(objectClass, "strValue", new TypeToken<String>(){}, SampleValue::strValue, Builder::strValue);
        public final PropertyMeta<SampleValue, Builder, Boolean> foo = PropertyMeta.create(objectClass, "foo", new TypeToken<Boolean>(){}, SampleValue::foo, Builder::foo);

        Meta() {
            propertyMap.put("intValue", intValue);
            propertyMap.put("doubleValue", doubleValue);
            propertyMap.put("strValue", strValue);
            propertyMap.put("foo", foo);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleValue> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleValue, Builder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleValue, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleValue, Builder, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleValue.builder();
        }

        @Override
        public int hashCode() {
            return Objects.hash(objectClass, builderClass);
        }

        @Override
        public boolean equals(Object obj) {
            return obj instanceof Meta
                    && Objects.equals(((Meta)obj).objectClass(), objectClass())
                    && Objects.equals(((Meta)obj).builderClass(), builderClass());
        }
    }

    @JsonIgnore
    public abstract Builder toBuilder();

    public static Builder builder() {
        return Builder.create();
    }

    @JsonCreator
    public static SampleValue create(
            @JsonProperty("intValue") int intValue,
            @JsonProperty("doubleValue") double doubleValue,
            @JsonProperty("strValue") String strValue,
            @JsonProperty("foo") boolean foo) {
        return SampleValue.builder()
                .intValue(intValue)
                .doubleValue(doubleValue)
                .strValue(strValue)
                .foo(foo)
                .build();
    }

    @Override
    public abstract int intValue();

    @Override
    @SampleFieldAnnotation(strValue = "test")
    public abstract double doubleValue();

    @Override
    @SampleFieldAnnotation
    @Nullable
    public abstract String strValue();

    @Override
    public abstract boolean foo();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleValue, Builder>, SampleValuePrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleValue.Builder();
        }

        @Override
        Builder intValue(int intValue);

        @Override
        @SampleFieldAnnotation(strValue = "test")
        Builder doubleValue(double doubleValue);

        @Override
        @SampleFieldAnnotation
        Builder strValue(String strValue);

        @Override
        Builder foo(boolean foo);
    }
}
