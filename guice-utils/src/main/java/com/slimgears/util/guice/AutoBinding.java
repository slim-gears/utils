package com.slimgears.util.guice;

import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Retention(RUNTIME)
@Target(TYPE)
public @interface AutoBinding {
    Class<? extends AutoBindingModule> module() default AutoBindingModule.class;
    Class<?>[] value();
}
