package com.google.auto.common;

import javax.lang.model.element.ExecutableElement;
import javax.lang.model.util.Elements;
import javax.lang.model.util.Types;

public class OverridesUtils {
    public static boolean nativeOverrides(Elements elements,
                                          ExecutableElement overrider,
                                          ExecutableElement overriden) {
        return new Overrides.NativeOverrides(elements).overrides(overrider, overriden, MoreElements.asType(overrider.getEnclosingElement()));
    }

    public static boolean explicitOverrides(Types types,
                                            ExecutableElement overrider,
                                            ExecutableElement overriden) {
        return new Overrides.ExplicitOverrides(types).overrides(overrider, overriden, MoreElements.asType(overrider.getEnclosingElement()));
    }
}
