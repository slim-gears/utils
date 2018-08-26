package com.slimgears.util.stream;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.slimgears.util.stream.Streams.self;

public class Tokenizer<E extends Enum<E>> {
    private final Pattern pattern;
    private final Set<E> tokenTypes;

    private Tokenizer(Map<E, String> patterns) {
        String regex = patterns.entrySet()
                .stream()
                .map(entry -> "(?<" + entry.getKey() + ">" + entry.getValue() + ")")
                .collect(Collectors.joining("|"));
        this.pattern = Pattern.compile(regex);
        this.tokenTypes = patterns.keySet();
    }

    public static class Token<E extends Enum<E>> {
        private final E type;
        private final String lexeme;

        Token(E type, String token) {
            this.type = type;
            this.lexeme = token;
        }

        public E type() {
            return type;
        }

        public String lexeme() {
            return lexeme;
        }
    }

    public Stream<Token<E>> tokenize(String input) {
        Matcher matcher = pattern.matcher(input);
        return Streams
                .takeWhile(Stream.generate(() -> nextToken(matcher)), Optional::isPresent)
                .map(Optional::get);
    }

    private Optional<Token<E>> nextToken(Matcher matcher) {
        if (!matcher.find()) {
            return Optional.empty();
        }

        return tokenTypes
                .stream()
                .map(type -> Optional
                        .ofNullable(matcher.group(type.name()))
                        .map(token -> new Token<>(type, token)))
                .filter(Optional::isPresent)
                .findFirst()
                .flatMap(self());
    }

    public static <E extends Enum<E>> Builder<E> builder() {
        return new Builder<>();
    }

    public static class Builder<E extends Enum<E>> {
        private final Map<E, String> patterns = new HashMap<>();

        public Builder<E> add(E key, String regex) {
            patterns.put(key, regex);
            return this;
        }

        public Builder<E> addAll(Class<E> keyClass, Function<E, String> patternGetter) {
            Stream.of(keyClass.getEnumConstants()).forEach(type -> add(type, patternGetter.apply(type)));
            return this;
        }

        public Tokenizer<E> build() {
            return new Tokenizer<>(patterns);
        }
    }
}
