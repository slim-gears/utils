package com.slimgears.util.generic;

import java.util.function.Supplier;

@SuppressWarnings("unused")
public class MoreStrings {
    public static Object lazy(Supplier<?> obj) {
        return LazyString.lazy(obj);
    }

    public static String format(String format, Object... args) {
        StringBuilder builder = new StringBuilder();
        int pos = 0;
        int prevPos = 0;
        int index = 0;
        while ((pos = format.indexOf("{}", pos)) >= 0) {
            builder.append(format, prevPos, pos);
            if (index < args.length) {
                builder.append(args[index++]);
            }
            pos += 2;
            prevPos = pos;
        }
        builder.append(format, prevPos, format.length());
        return builder.toString();
    }
}

