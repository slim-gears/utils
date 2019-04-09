package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseAutoValueAnnotator;
import com.slimgears.util.autovalue.annotations.UseCreatorExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;
import com.slimgears.util.autovalue.annotations.UseJacksonAnnotator;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@AutoValuePrototype(
        pattern = "(.*)Proto(.*)",
        value = "$1ConcreteWithCreator$2")
@UseJacksonAnnotator
@UseAutoValueAnnotator
@UseCreatorExtension
@interface CustomAutoAnnotation {

}