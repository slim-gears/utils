package com.slimgears.util.reflect.internal;

public class Require {
    public static void argument(boolean condition, String message) {
        if (!condition) {
            throw new IllegalArgumentException(message);
        }
    }

    public static void state(boolean condition, String message) {
        if (!condition) {
            throw new IllegalStateException(message);
        }
    }
}
