package com.slimgears.util.stream;

import java.util.regex.Pattern;

public class Patterns {
    public static Pattern fromWildcard(String wildcard) {
        String regex = "^" + wildcard
                .replace(".", "\\.")
                .replace("?", ".")
                .replace("\\*", ".*") + "$";
        return Pattern.compile(regex);
    }
}
