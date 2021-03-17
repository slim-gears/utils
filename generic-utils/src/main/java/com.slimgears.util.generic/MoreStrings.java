package com.slimgears.util.generic;

import java.util.Collection;
import java.util.function.Supplier;
import java.util.stream.Collectors;

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
                builder.append(argToString(args[index++]));
            }
            pos += 2;
            prevPos = pos;
        }
        builder.append(format, prevPos, format.length());
        return builder.toString();
    }

    private static String argToString(Object arg) {
        if (arg instanceof Collection) {
            return ((Collection<?>)arg).stream().map(String::valueOf)
                    .collect(Collectors.joining(", ", "[", "]"));
        }
        return String.valueOf(arg);
    }
}

