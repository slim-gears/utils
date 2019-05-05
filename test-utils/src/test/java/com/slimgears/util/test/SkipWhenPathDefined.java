package com.slimgears.util.test;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

@SkipWhenEnvDefined("PATH")
@Retention(RetentionPolicy.RUNTIME)
@interface SkipWhenPathDefined {
}
