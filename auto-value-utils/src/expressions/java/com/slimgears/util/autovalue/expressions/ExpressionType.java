package com.slimgears.util.autovalue.expressions;

public enum ExpressionType {
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

    ExpressionType(Class<? extends Expression> type) {
        this.type = type;
    }

    public Class<? extends Expression> type() {
        return this.type;
    }

    private final Class<? extends Expression> type;
}
