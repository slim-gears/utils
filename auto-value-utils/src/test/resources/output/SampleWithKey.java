package com.slimgears.sample;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.auto.value.AutoValue;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.Key;
import com.slimgears.util.autovalue.annotations.MetaClassWithKey;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.PropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.StringPropertyExpression;
import com.slimgears.util.reflect.TypeToken;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import javax.annotation.Generated;

@Generated("com.slimgears.util.autovalue.apt.AutoValuePrototypeAnnotationProcessor")
@AutoValue
public abstract class SampleWithKey implements SampleWithKeyPrototype {

    public static final Expressions<SampleWithKey> $ = new Expressions<>();
    public static Expressions<SampleWithKey> $() {
        return $;
    }

    public static class Expressions<__S> {
        private final ObjectExpression<__S, SampleWithKey> self = ObjectExpression.arg();
        private final Meta meta = new Meta() ;

        public final StringPropertyExpression<__S, SampleWithKey, Builder> id = PropertyExpression.ofString(self, meta.id);
        public final StringPropertyExpression<__S, SampleWithKey, Builder> text = PropertyExpression.ofString(self, meta.text);
        public final NumericPropertyExpression<__S, SampleWithKey, Builder, Integer> number = PropertyExpression.ofNumeric(self, meta.number);
    }

    public static class ReferencePropertyExpression<__S, __T, __B> extends Expressions<__S> implements PropertyExpression<__S, __T, __B, SampleWithKey> {
        private final ObjectExpression<__S, __T> target;
        private final PropertyMeta<__T, __B, SampleWithKey> property;

        private ReferencePropertyExpression(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleWithKey> property) {
            this.target = target;
            this.property = property;
        }

        static <__S, __T, __B> ReferencePropertyExpression<__S, __T, __B> create(ObjectExpression<__S, __T> target, PropertyMeta<__T, __B, SampleWithKey> property) {
            return new ReferencePropertyExpression<>(target, property);
        }

        @Override
        public ObjectExpression<__S, __T> target() {
            return target;
        }

        @Override
        public PropertyMeta<__T, __B, SampleWithKey> property() {
            return property;
        }

        @Override
        public Type type() {
            return Type.Property;
        }
    }

    public MetaClassWithKey<String, SampleWithKey, SampleWithKey.Builder> metaClass() {
        return metaClass;
    }

    public static final Meta metaClass = new Meta();
    public static class Meta implements MetaClassWithKey<String, SampleWithKey, SampleWithKey.Builder> {
        private final TypeToken<SampleWithKey> objectClass = new TypeToken<SampleWithKey>(){};
        private final TypeToken<Builder> builderClass = new TypeToken<Builder>(){};
        private final Map<String, PropertyMeta<SampleWithKey, Builder, ?>> propertyMap = new LinkedHashMap<>();

        public final PropertyMeta<SampleWithKey, Builder, String> id = PropertyMeta.create(objectClass, "id", new TypeToken<String>(){}, SampleWithKey::id, Builder::id);
        public final PropertyMeta<SampleWithKey, Builder, String> text = PropertyMeta.create(objectClass, "text", new TypeToken<String>(){}, SampleWithKey::text, Builder::text);
        public final PropertyMeta<SampleWithKey, Builder, Integer> number = PropertyMeta.create(objectClass, "number", new TypeToken<Integer>(){}, SampleWithKey::number, Builder::number);

        @Override
        public PropertyMeta<SampleWithKey, Builder, String> keyProperty() {
            return id;
        }

        Meta() {
            propertyMap.put("id", id);
            propertyMap.put("text", text);
            propertyMap.put("number", number);
        }

        @Override
        public TypeToken<Builder> builderClass() {
            return this.builderClass;
        }

        @Override
        public TypeToken<SampleWithKey> objectClass() {
            return this.objectClass;
        }

        @Override
        public Iterable<PropertyMeta<SampleWithKey, Builder, ?>> properties() {
            return propertyMap.values();
        }

        @Override
        public <__V> PropertyMeta<SampleWithKey, Builder, __V> getProperty(String name) {
            //noinspection unchecked
            return (PropertyMeta<SampleWithKey, Builder, __V>)propertyMap.get(name);
        }

        @Override
        public Builder createBuilder() {
            return SampleWithKey.builder();
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
    public static SampleWithKey create(
            @JsonProperty("id") String id,
            @JsonProperty("text") String text,
            @JsonProperty("number") int number) {
        return SampleWithKey.builder()
                .id(id)
                .text(text)
                .number(number)
                .build();
    }

    @Override
    @Key
    public abstract String id();

    @Override
    public abstract String text();

    @Override
    public abstract int number();

    @AutoValue.Builder
    public interface Builder extends BuilderPrototype<SampleWithKey, Builder>, SampleWithKeyPrototypeBuilder<Builder> {
        public static Builder create() {
            return new AutoValue_SampleWithKey.Builder();
        }

        @Override
        @Key
        Builder id(String id);

        @Override
        Builder text(String text);

        @Override
        Builder number(int number);
    }
}
