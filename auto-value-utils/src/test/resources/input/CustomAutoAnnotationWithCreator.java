package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.AutoValueCreator;
import com.slimgears.util.autovalue.annotations.AutoValueMetaData;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@AutoValuePrototype(
        pattern = "(.*)Proto(.*)",
        value = "$1Concrete$2",
        annotators = "com.slimgears.util.autovalue.apt.extensions.JacksonAnnotator")
@AutoValueCreator
@interface CustomAutoAnnotation {

}