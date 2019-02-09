package com.slimgears.util.autovalue.expressions;

import com.slimgears.util.autovalue.annotations.BuilderPrototype;
import com.slimgears.util.autovalue.annotations.HasMetaClass;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import javafx.beans.binding.StringExpression;

public interface StringPropertyExpression<T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> extends ComparablePropertyExpression<T, TB, String>, StringValueExpression {
    static <T extends HasMetaClass<T, TB>, TB extends BuilderPrototype<T, TB>> StringPropertyExpression<T, TB> of(PropertyMeta<T, TB, String> property) {
        return new StringPropertyExpression<T, TB>() {
            @Override
            public PropertyMeta<T, TB, String> property() {
                return property;
            }

            @Override
            public String type() {
                return "stringProperty";
            }
        };
    }
}
