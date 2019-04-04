package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseCreatorExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@AutoValuePrototype(
        pattern = "(.*)Proto(.*)",
        value = "$1ConcreteWithCreator$2",
        annotators = "com.slimgears.util.autovalue.apt.extensions.JacksonAnnotator")
@UseCreatorExtension
@interface CustomAutoAnnotation {

}