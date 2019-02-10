package com.slimgears.util.autovalue.expressions;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;

public interface StringPropertyExpression<T, B> extends ComparablePropertyExpression<T, B, String>, StringExpression {
    @JsonCreator
    static <T, B> StringPropertyExpression<T, B> of(
            @JsonProperty("target") Expression<T> target,
            @JsonProperty("property") PropertyMeta<T, B, String> property) {
        return new StringPropertyExpression<T, B>() {
            @Override
            public Expression<T> target() {
                return target;
            }

            @Override
            public PropertyMeta<T, B, String> property() {
                return property;
            }

            @Override
            public ExpressionType type() {
                return ExpressionType.StringProperty;
            }
        };
    }
}
