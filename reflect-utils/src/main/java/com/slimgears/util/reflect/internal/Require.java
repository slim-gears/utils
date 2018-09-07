package com.slimgears.util.reflect.internal;

import java.util.function.Supplier;

public class Require {
    public static void argument(boolean condition, String message) {
        argument(condition, () -> message);
    }

    public static void argument(boolean condition, Supplier<String> message) {
        if (!condition) {
            throw new IllegalArgumentException(message.get());
        }
    }

    public static void state(boolean condition, String message) {
        state(condition, () -> message);
    }

    public static void state(boolean condition, Supplier<String> message) {
        if (!condition) {
            throw new IllegalStateException(message.get());
        }
    }
}
