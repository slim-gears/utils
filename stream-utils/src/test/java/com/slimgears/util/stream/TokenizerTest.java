package com.slimgears.util.stream;

import org.junit.Test;

import java.util.Collection;
import java.util.stream.Collectors;

public class TokenizerTest {
    enum Token {
        Identifier("[a-zA-Z0-9_.]+"),
        BeginArgs("\\<"),
        EndArgs("\\>"),
        ArgSeparator(","),
        WhiteSpace("[ \\t\\n\\r]*");


        private final String pattern;

        Token(String pattern) {
            this.pattern = pattern;
        }

        public String pattern() {
            return pattern;
        }
    }

    @Test
    public void testTokenizer() {
        Tokenizer<Token> tokenizer = Tokenizer.<Token>builder()
                .addAll(Token.class, Token::pattern)
                .build();

        Collection<Tokenizer.Token<Token>> tokens = tokenizer
                .tokenize("Map<String, List<Integer>>")
                .collect(Collectors.toList());
    }
}