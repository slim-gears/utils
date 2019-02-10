package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;

import java.util.Collection;

@JsonTypeIdResolver(ExpressionTypeResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", visible = true)
//@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, property = "type", visible = true)
//@JsonSubTypes({
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "And"),
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "Or"),
//        @JsonSubTypes.Type(value = BooleanUnaryOperationExpression.class, name = "Not"),
//
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "Equals"),
//        @JsonSubTypes.Type(value = BooleanUnaryOperationExpression.class, name = "IsNull"),
//
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "LessThan"),
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "GreaterThan"),
//
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "Contains"),
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "StartsWith"),
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "EndsWith"),
//        @JsonSubTypes.Type(value = BooleanBinaryOperationExpression.class, name = "Matches"),
//
//        @JsonSubTypes.Type(value = NumericBinaryOperationExpression.class, name = "Add"),
//        @JsonSubTypes.Type(value = NumericBinaryOperationExpression.class, name = "Sub"),
//        @JsonSubTypes.Type(value = NumericBinaryOperationExpression.class, name = "Mul"),
//        @JsonSubTypes.Type(value = NumericBinaryOperationExpression.class, name = "Div"),
//        @JsonSubTypes.Type(value = NumericUnaryOperationExpression.class, name = "Negate"),
//
//        @JsonSubTypes.Type(value = PropertyExpression.class, name = "Property"),
//        @JsonSubTypes.Type(value = ComparablePropertyExpression.class, name = "ComparableProperty"),
//        @JsonSubTypes.Type(value = NumericPropertyExpression.class, name = "NumericProperty"),
//        @JsonSubTypes.Type(value = StringPropertyExpression.class, name = "StringProperty"),
//
//        @JsonSubTypes.Type(value = ConstantExpression.class, name = "Constant"),
//        @JsonSubTypes.Type(value = NumericConstantExpression.class, name = "NumericConstant"),
//        @JsonSubTypes.Type(value = StringConstantExpression.class, name = "StringConstant"),
//})
public interface Expression<V> {
    ExpressionType type();

    default BooleanExpression eq(Expression<V> value) {
        return BooleanBinaryOperationExpression.create(ExpressionType.Equals, this, value);
    }

    default BooleanExpression eq(V value) {
        return eq(ConstantExpression.of(value));
    }

    default BooleanExpression notEq(Expression<V> value) {
        return eq(value).not();
    }

    default BooleanExpression notEq(V value) {
        return eq(value).not();
    }

    default BooleanExpression isNull() {
        return BooleanUnaryOperationExpression.create(ExpressionType.IsNull, this);
    }

    default BooleanExpression isNotNull() {
        return isNull().not();
    }

    default BooleanExpression inArray(Expression<V[]> values) {
        return BooleanBinaryOperationExpression.create(ExpressionType.ValueInArray, this, values);
    }

    default BooleanExpression inArray(V... values) {
        return inArray(ConstantExpression.of(values));
    }

    default BooleanExpression in(Expression<Collection<V>> values) {
        return BooleanBinaryOperationExpression.create(ExpressionType.ValueIn, this, values);
    }

    default BooleanExpression in(Collection<V> values) {
        return in(ConstantExpression.of(values));
    }
}
