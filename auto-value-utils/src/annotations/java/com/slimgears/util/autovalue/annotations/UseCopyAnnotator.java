package com.slimgears.util.autovalue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
@AutoValuePrototype.Annotator("com.slimgears.util.autovalue.apt.extensions.CopyAnnotationsAnnotator")
public @interface UseCopyAnnotator {
}
