package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.databind.annotation.JsonTypeIdResolver;
import com.slimgears.util.autovalue.expressions.internal.*;

import java.util.Optional;
import java.util.function.Function;

@JsonTypeIdResolver(ExpressionTypeResolver.class)
@JsonTypeInfo(use = JsonTypeInfo.Id.CUSTOM, property = "type", visible = true)
public interface Expression<S> {
    Type type();

    enum Type {
        And(BooleanBinaryOperationExpression.class),
        Or(BooleanBinaryOperationExpression.class),
        Not(BooleanUnaryOperationExpression.class),

        Equals(BooleanBinaryOperationExpression.class),
        IsNull(BooleanUnaryOperationExpression.class),

        ValueIn(BooleanBinaryOperationExpression.class),

        LessThan(BooleanBinaryOperationExpression.class),
        GreaterThan(BooleanBinaryOperationExpression.class),

        IsEmpty(BooleanUnaryOperationExpression.class),
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
        BooleanProperty(BooleanPropertyExpression.class),
        CollectionProperty(CollectionPropertyExpression.class),

        Constant(ConstantExpression.class),
        ComparableConstant(ComparableConstantExpression.class),
        NumericConstant(NumericConstantExpression.class),
        StringConstant(StringConstantExpression.class),
        BooleanConstant(BooleanConstantExpression.class),
        CollectionConstant(CollectionConstantExpression.class),

        Composition(ObjectComposedExpression.class),
        ComparableComposition(ComparableComposedExpression.class),
        NumericComposition(NumericComposedExpression.class),
        StringComposition(StringComposedExpression.class),
        BooleanComposition(BooleanComposedExpression.class),
        CollectionComposition(CollectionComposedExpression.class),

        MapCollection(CollectionOperationExpression.class),
        FlatMapCollection(CollectionOperationExpression.class),
        FilterCollection(CollectionOperationExpression.class),

        Count(ObjectBinaryOperationExpression.class),
        Min(ComparableBinaryOperationExpression.class),
        Max(ComparableBinaryOperationExpression.class),
        Average(NumericBinaryOperationExpression.class),
        Sum(NumericBinaryOperationExpression.class),

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
