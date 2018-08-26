/**
 *
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;

import java.util.Optional;
import java.util.function.Predicate;
import java.util.regex.Pattern;
import java.util.stream.Stream;

public class TypeFilters {
    public static Predicate<TypeInfo> fromIncludedExcludedWildcard(String included, String excluded) {
        return fromIncludedWildcard(included).and(fromExcludedWildcard(excluded));
    }

    public static Predicate<TypeInfo> fromExcludedWildcard(String wildcard) {
        return fromWildcard(wildcard, false).negate();
    }

    public static Predicate<TypeInfo> fromIncludedWildcard(String wildcards) {
        return fromWildcard(wildcards, true);
    }


    private static Predicate<TypeInfo> fromWildcard(String wildcards, boolean defaultVal) {
        return Optional
                .ofNullable(wildcards)
                .filter(w -> !w.isEmpty())
                .map(w -> w.split(","))
                .map(Stream::of)
                .orElseGet(Stream::empty)
                .map(String::trim)
                .map(TypeFilters::fromWildcard)
                .map(Pattern::asPredicate)
                .map(TypeFilters::fromStringPredicate)
                .reduce(Predicate::or)
                .orElse(t -> defaultVal);
    }

    private static Predicate<TypeInfo> fromStringPredicate(Predicate<String> predicate) {
        return type -> predicate.test(type.name());
    }

    private static Pattern fromWildcard(String wildcard) {
        String regex = "^" + wildcard
                .replace(".", "\\.")
                .replace("?", ".")
                .replace("*", ".*") + "$";
        return Pattern.compile(regex);
    }
}
