package com.slimgears.sample;

import com.slimgears.util.autovalue.annotations.UseBuilderExtension;
import com.slimgears.util.autovalue.annotations.UseJacksonAnnotator;
import com.slimgears.util.autovalue.annotations.UseMetaDataExtension;
import com.slimgears.util.autovalue.annotations.AutoValuePrototype;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Retention(RetentionPolicy.SOURCE)
@Target(ElementType.TYPE)
@AutoValuePrototype(
        pattern = "(.*)Proto(.*)",
        value = "$1ConcreteWithBuilder$2")
@UseMetaDataExtension
@UseJacksonAnnotator
@UseBuilderExtension
@interface CustomAutoAnnotation {

}