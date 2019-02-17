/**
 *
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;
import com.slimgears.util.reflect.internal.TypeTokenLexer;
import com.slimgears.util.reflect.internal.TypeTokenParser;
import com.slimgears.util.stream.Optionals;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Optional;

public class TypeTokenParserAdapter {
    public static TypeInfo toTypeInfo(String str) {
        TypeTokenLexer lexer = new TypeTokenLexer(CharStreams.fromString(str));
        lexer.removeErrorListeners();
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        TypeTokenParser parser = new TypeTokenParser(tokenStream);
        parser.removeErrorListeners();
        parser.removeParseListeners();
        TypeInfo typeInfo = toTypeInfo(parser.type());

        typeInfo = (parser.getNumberOfSyntaxErrors() > 0)
                ? TypeInfo.builder().name(str).build()
                : typeInfo;

        String[] parts = typeInfo.name().split("\\$");

        if (parts.length > 1) {
            typeInfo = typeInfo
                    .toBuilder()
                    .enclosingType(toTypeInfo(parts[0]))
                    .build();
        }

        return typeInfo;
    }

    private static TypeInfo toTypeInfo(TypeTokenParser.TypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.primitiveType()).map(TypeTokenParserAdapter::toTypeInfo),
                () -> Optional.ofNullable(ctx.referenceType()).map(TypeTokenParserAdapter::toTypeInfo))
                .orElse(null);
    }

    private static TypeInfo toTypeInfo(TypeTokenParser.PrimitiveTypeContext ctx) {
        return TypeInfo.ofPrimitive(ctx.getText());
    }

    private static TypeInfo toTypeInfo(TypeTokenParser.ReferenceTypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.classOrInterfaceType()).map(TypeTokenParserAdapter::toTypeInfo),
                () -> Optional.ofNullable(ctx.arrayType()).map(TypeTokenParserAdapter::toTypeInfo),
                () -> Optional.ofNullable(ctx.typeVariable()).map(TypeTokenParserAdapter::toTypeInfo))
                .orElse(null);
    }

    private static TypeInfo toTypeInfo(TypeTokenParser.ClassOrInterfaceTypeContext ctx) {
        return TypeInfo
                .builder()
                .name(ctx.TypeIdentifier().getText())
                .typeParams(Optional.ofNullable(ctx.classOrInterfaceArgs())
                        .map(args -> args.typeArgument().stream().map(TypeTokenParserAdapter::toTypeParamInfo).toArray(TypeParameterInfo[]::new))
                        .orElseGet(() -> new TypeParameterInfo[0]))
                .build();
    }

    private static TypeParameterInfo toTypeParamInfo(TypeTokenParser.TypeArgumentContext typeArgument) {
        return Optionals.or(
                () -> Optional.ofNullable(typeArgument.referenceType()).map(TypeTokenParserAdapter::toTypeInfo).map(t -> TypeParameterInfo.of("", t)),
                () -> Optional.ofNullable(typeArgument.wildcard()).map(TypeTokenParserAdapter::toTypeParamInfo))
                .orElse(null);
    }

    private static TypeParameterInfo toTypeParamInfo(TypeTokenParser.WildcardContext ctx) {
        TypeParameterInfo.Builder builder = TypeParameterInfo.builder().name("?").type(TypeInfo.ofWildcard());
        Optional.ofNullable(ctx.wildcardBounds())
                .flatMap(btcx -> Optionals.or(
                        () -> Optional.ofNullable(btcx.wildcardExtendBound())
                                .map(TypeTokenParser.WildcardExtendBoundContext::referenceType)
                                .map(TypeTokenParserAdapter::toTypeInfo)
                                .map(TypeParameterInfo.BoundInfo::ofBoundExtends),
                        () -> Optional.ofNullable(btcx.wildcardSuperBound())
                                .map(TypeTokenParser.WildcardSuperBoundContext::referenceType)
                                .map(TypeTokenParserAdapter::toTypeInfo)
                                .map(TypeParameterInfo.BoundInfo::ofBoundSuper)))
                .ifPresent(builder::bounding);
        return builder.build();
    }

    private static TypeInfo toTypeInfo(TypeTokenParser.ArrayTypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.classOrInterfaceType()).map(TypeTokenParserAdapter::toTypeInfo).map(type -> TypeInfo.arrayOf(type, ctx.dim().size())),
                () -> Optional.ofNullable(ctx.primitiveType()).map(TypeTokenParserAdapter::toTypeInfo).map(type -> TypeInfo.arrayOf(type, ctx.dim().size())),
                () -> Optional.ofNullable(ctx.typeVariable()).map(TypeTokenParserAdapter::toTypeInfo).map(type -> TypeInfo.arrayOf(type, ctx.dim().size())))
                .orElse(null);
    }

    private static TypeInfo toTypeInfo(TypeTokenParser.TypeVariableContext ctx) {
        return TypeInfo.of(ctx.Identifier().getSymbol().getText());
    }
}
