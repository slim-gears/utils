package com.slimgears.util.autovalue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * Annotation used to generate AutoValue classes from interfaces
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface AutoValuePrototype {
    /**
     * Generated AutoValue class name (may use regular expression groups, matched by `pattern`
     * @return Class name expression
     */
    String value() default "$1";

    /**
     * Regular expression, which matches the input name. Can contain regular expression groups
     * which can be referenced in `value`
     * @return Regular expression
     */
    String pattern() default "(.*)Prototype";

    /**
     * Annotation, used to mark custom builders base interfaces for generated AutoValue builders
     */
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.TYPE)
    @interface Builder {}
}
