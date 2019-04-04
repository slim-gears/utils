package com.slimgears.util.autovalue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoValuePrototype.Extension("com.slimgears.util.autovalue.apt.extensions.BuilderExtension")
public @interface UseBuilderExtension {
}
