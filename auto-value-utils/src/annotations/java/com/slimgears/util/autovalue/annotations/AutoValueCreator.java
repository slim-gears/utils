package com.slimgears.util.autovalue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@AutoValuePrototype.Extension("com.slimgears.util.autovalue.apt.extensions.CreatorExtension")
public @interface AutoValueCreator {
}
