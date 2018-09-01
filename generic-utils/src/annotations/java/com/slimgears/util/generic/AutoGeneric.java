/**
 *
 */
package com.slimgears.util.generic;

public @interface AutoGeneric {
    String className();
    WithParams[] implementations();

    @interface WithParams {
        Class[] value();
    }

    @interface ClassParam {
        String value();
    }
}
