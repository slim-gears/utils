package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.slimgears.util.autovalue.expressions.internal.ArgumentExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.BooleanUnaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.ComparableConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.ComparablePropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.ExpressionTypeResolver;
import com.slimgears.util.autovalue.expressions.internal.NumericBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.NumericUnaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.StringBinaryOperationExpression;
import com.slimgears.util.autovalue.expressions.internal.StringConstantExpression;
import com.slimgears.util.autovalue.expressions.internal.StringPropertyExpression;
import com.slimgears.util.autovalue.expressions.internal.StringUnaryOperationExpression;

import java.util.Optional;
import java.util.function.Function;

@JsonTypeIdResolver(ExpressionTypeResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", visible = true)
public interface Expression {
    Type type();

    enum Type {
        And(BooleanBinaryOperationExpression.class),
        Or(BooleanBinaryOperationExpression.class),
        Not(BooleanUnaryOperationExpression.class),

        Equals(BooleanBinaryOperationExpression.class),
        IsNull(BooleanUnaryOperationExpression.class),

        ValueInArray(BooleanBinaryOperationExpression.class),
        ValueIn(BooleanBinaryOperationExpression.class),

        LessThan(BooleanBinaryOperationExpression.class),
        GreaterThan(BooleanBinaryOperationExpression.class),

        Contains(BooleanBinaryOperationExpression.class),
        StartsWith(BooleanBinaryOperationExpression.class),
        EndsWith(BooleanBinaryOperationExpression.class),
        Matches(BooleanBinaryOperationExpression.class),
        Length(NumericUnaryOperationExpression.class),
        Concat(StringBinaryOperationExpression.class),
        ToLower(StringUnaryOperationExpression.class),
        ToUpper(StringUnaryOperationExpression.class),
        Trim(StringUnaryOperationExpression.class),

        Add(NumericBinaryOperationExpression.class),
        Sub(NumericBinaryOperationExpression.class),
        Negate(NumericUnaryOperationExpression.class),
        Mul(NumericBinaryOperationExpression.class),
        Div(NumericBinaryOperationExpression.class),

        Property(PropertyExpression.class),
        ComparableProperty(ComparablePropertyExpression.class),
        NumericProperty(ComparablePropertyExpression.class),
        StringProperty(StringPropertyExpression.class),

        Constant(ConstantExpression.class),
        ComparableConstant(ComparableConstantExpression.class),
        NumericConstant(NumericConstantExpression.class),
        StringConstant(StringConstantExpression.class),

        Argument(ArgumentExpression.class);

        Type(Class<? extends Expression> type) {
            this.type = type;
        }

        public Class<? extends Expression> type() {
            return this.type;
        }

        @JsonCreator
        public static Type fromString(String key) {
            return Type.valueOf(modifyCase(key, Character::toUpperCase));
        }

        private final Class<? extends Expression> type;

        @Override
        public String toString() {
            return modifyCase(super.toString(), Character::toLowerCase);
        }

        private static String modifyCase(String name, Function<Character, Character> modifier) {
            return Optional.ofNullable(name)
                    .filter(n -> !n.isEmpty())
                    .map(n -> modifier.apply(n.charAt(0)) + n.substring(1))
                    .orElse(name);
        }
    }
}
