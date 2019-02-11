/**
 *
 */
package com.slimgears.util.reflect.internal;

import com.slimgears.util.reflect.TypeToken;
import com.slimgears.util.stream.Optionals;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.lang.reflect.Type;
import java.util.Optional;
import java.util.stream.IntStream;
import java.util.stream.Stream;

public class TypeTokenParserAdapter {
    public static TypeToken toTypeToken(String str) {
        TypeTokenLexer lexer = new TypeTokenLexer(CharStreams.fromString(str));
//        lexer.removeErrorListeners();
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TypeTokenParser parser = new TypeTokenParser(tokenStream);
//        parser.removeErrorListeners();
//        parser.removeParseListeners();
        return toTypeToken(parser.type());
    }

    private static TypeToken toTypeToken(TypeTokenParser.TypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.primitiveType()).map(TypeTokenParserAdapter::toTypeToken),
                () -> Optional.ofNullable(ctx.referenceType()).map(TypeTokenParserAdapter::toTypeToken))
                .orElse(null);
    }

    private static TypeToken toTypeToken(TypeTokenParser.PrimitiveTypeContext ctx) {
        try {
            return TypeToken.ofType(Class.forName(ctx.getText()));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static TypeToken toTypeToken(TypeTokenParser.ReferenceTypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.classOrInterfaceType()).map(TypeTokenParserAdapter::toTypeToken),
                () -> Optional.ofNullable(ctx.arrayType()).map(TypeTokenParserAdapter::toTypeToken),
                () -> Optional.ofNullable(ctx.typeVariable()).map(TypeTokenParserAdapter::toTypeToken))
                .orElse(null);
    }

    private static TypeToken toTypeToken(TypeTokenParser.ClassOrInterfaceTypeContext ctx) {
        try {
            Class rawClass = Class.forName(ctx.TypeIdentifier().getText());
            return TypeToken.ofParameterized(
                    rawClass,
                    Optional
                            .ofNullable(ctx.classOrInterfaceArgs())
                            .map(args -> args.typeArgument().stream().map(TypeTokenParserAdapter::toTypeParamInfo))
                            .orElse(Stream.empty())
                            .toArray(TypeToken<?>[]::new));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    private static TypeToken toTypeParamInfo(TypeTokenParser.TypeArgumentContext typeArgument) {
        return Optionals.or(
                () -> Optional.ofNullable(typeArgument.referenceType()).map(TypeTokenParserAdapter::toTypeToken),
                () -> Optional.ofNullable(typeArgument.wildcard()).map(TypeTokenParserAdapter::toTypeParamInfo))
                .orElse(null);
    }

    private static TypeToken toTypeParamInfo(TypeTokenParser.WildcardContext ctx) {
        return Optional.ofNullable(ctx.wildcardBounds())
                .map(bctx -> {
                    Type[] upperBounds = Optional.ofNullable(bctx.wildcardExtendBound())
                            .map(TypeTokenParser.WildcardExtendBoundContext::referenceType)
                            .map(TypeTokenParserAdapter::toTypeToken)
                            .map(TypeToken::type)
                            .map(t -> new Type[]{t})
                            .orElseGet(() -> new Type[0]);
                    Type[] lowerBounds = Optional.ofNullable(bctx.wildcardSuperBound())
                            .map(TypeTokenParser.WildcardSuperBoundContext::referenceType)
                            .map(TypeTokenParserAdapter::toTypeToken)
                            .map(TypeToken::type)
                            .map(t -> new Type[]{t})
                            .orElseGet(() -> new Type[0]);
                    return TypeToken.ofWildcard(lowerBounds, upperBounds);
                })
                .orElseGet(TypeToken::ofWildcard);
    }

    private static TypeToken toTypeToken(TypeTokenParser.ArrayTypeContext ctx) {
        //noinspection unchecked
        return Optionals.<TypeToken>or(
                () -> Optional.ofNullable(ctx.classOrInterfaceType()).map(TypeTokenParserAdapter::toTypeToken).map(TypeToken::toArray),
                () -> Optional.ofNullable(ctx.primitiveType()).map(TypeTokenParserAdapter::toTypeToken).map(TypeToken::toArray),
                () -> Optional.ofNullable(ctx.typeVariable()).map(TypeTokenParserAdapter::toTypeToken).map(TypeToken::toArray))
                .flatMap(t -> IntStream.range(0, ctx.dim().size()).mapToObj(i -> t).reduce((t1, t2) -> t1.toArray()))
                .orElse(null);
    }

    private static TypeToken toTypeToken(TypeTokenParser.TypeVariableContext ctx) {
        throw new IllegalStateException("Cannot handle type variables: " + ctx.getText());
    }
}
