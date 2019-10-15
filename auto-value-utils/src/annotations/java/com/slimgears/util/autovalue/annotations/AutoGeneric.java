/**
 *
 */
package com.slimgears.util.autovalue.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Target;

public @interface AutoGeneric {
    String className();
    Variant[] variants();

    @Target(ElementType.TYPE)
    @interface Variant {
        Class[] value();
    }

    @interface ClassParam {
        String value();
    }
}
