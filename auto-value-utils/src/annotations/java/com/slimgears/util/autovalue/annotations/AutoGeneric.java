/**
 *
 */
package com.slimgears.util.autovalue.annotations;

public @interface AutoGeneric {
    String className();
    Variant[] variants();

    @interface Variant {
        Class[] value();
    }

    @interface ClassParam {
        String value();
    }
}
