/**
 *
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import com.slimgears.apt.data.TypeParameterInfo;
import com.slimgears.rxrpc.apt.typeinfo.TypeInfoLexer;
import com.slimgears.rxrpc.apt.typeinfo.TypeInfoParser;
import com.slimgears.util.stream.Optionals;
import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;

import java.util.Optional;
import java.util.function.Supplier;

public class TypeInfoParserAdapter {
    private final static Supplier<RuntimeException> invalidState = () -> new RuntimeException("Invalid parser state");

    public static TypeInfo toTypeInfo(String str) {
        TypeInfoLexer lexer = new TypeInfoLexer(CharStreams.fromString(str));
        CommonTokenStream tokenStream = new CommonTokenStream(lexer);
        com.slimgears.rxrpc.apt.typeinfo.TypeInfoParser parser = new TypeInfoParser(tokenStream);
        return toTypeInfo(parser.type());
    }

    private static TypeInfo toTypeInfo(TypeInfoParser.TypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.primitiveType()).map(TypeInfoParserAdapter::toTypeInfo),
                () -> Optional.ofNullable(ctx.referenceType()).map(TypeInfoParserAdapter::toTypeInfo))
                .orElseThrow(invalidState);
    }

    private static TypeInfo toTypeInfo(TypeInfoParser.PrimitiveTypeContext ctx) {
        return TypeInfo.ofPrimitive(ctx.getText());
    }

    private static TypeInfo toTypeInfo(TypeInfoParser.ReferenceTypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.classOrInterfaceType()).map(TypeInfoParserAdapter::toTypeInfo),
                () -> Optional.ofNullable(ctx.arrayType()).map(TypeInfoParserAdapter::toTypeInfo),
                () -> Optional.ofNullable(ctx.typeVariable()).map(TypeInfoParserAdapter::toTypeInfo))
                .orElseThrow(invalidState);
    }

    private static TypeInfo toTypeInfo(TypeInfoParser.ClassOrInterfaceTypeContext ctx) {
        return TypeInfo
                .builder()
                .name(ctx.TypeIdentifier().getText())
                .typeParams(Optional.ofNullable(ctx.classOrInterfaceArgs())
                        .map(args -> args.typeArgument().stream().map(TypeInfoParserAdapter::toTypeParamInfo).toArray(TypeParameterInfo[]::new))
                        .orElseGet(() -> new TypeParameterInfo[0]))
                .build();
    }

    private static TypeParameterInfo toTypeParamInfo(TypeInfoParser.TypeArgumentContext typeArgument) {
        return Optionals.or(
                () -> Optional.ofNullable(typeArgument.referenceType()).map(TypeInfoParserAdapter::toTypeInfo).map(t -> TypeParameterInfo.of("", t)),
                () -> Optional.ofNullable(typeArgument.wildcard()).map(TypeInfoParserAdapter::toTypeParamInfo)).orElseThrow(invalidState);
    }

    private static TypeParameterInfo toTypeParamInfo(TypeInfoParser.WildcardContext ctx) {
        TypeParameterInfo.Builder builder = TypeParameterInfo.builder().name("?").type(TypeInfo.ofWildcard());
        Optional.ofNullable(ctx.wildcardBounds())
                .flatMap(btcx -> Optionals.or(
                        () -> Optional.ofNullable(btcx.wildcardExtendBound())
                                .map(TypeInfoParser.WildcardExtendBoundContext::referenceType)
                                .map(TypeInfoParserAdapter::toTypeInfo)
                                .map(TypeParameterInfo.BoundInfo::ofBoundExtends),
                        () -> Optional.ofNullable(btcx.wildcardSuperBound())
                                .map(TypeInfoParser.WildcardSuperBoundContext::referenceType)
                                .map(TypeInfoParserAdapter::toTypeInfo)
                                .map(TypeParameterInfo.BoundInfo::ofBoundSuper)))
                .ifPresent(builder::bounding);
        return builder.build();
    }

    private static TypeInfo toTypeInfo(TypeInfoParser.ArrayTypeContext ctx) {
        return Optionals.or(
                () -> Optional.ofNullable(ctx.classOrInterfaceType()).map(TypeInfoParserAdapter::toTypeInfo).map(type -> TypeInfo.arrayOf(type, ctx.dim().size())),
                () -> Optional.ofNullable(ctx.primitiveType()).map(TypeInfoParserAdapter::toTypeInfo).map(type -> TypeInfo.arrayOf(type, ctx.dim().size())),
                () -> Optional.ofNullable(ctx.typeVariable()).map(TypeInfoParserAdapter::toTypeInfo).map(type -> TypeInfo.arrayOf(type, ctx.dim().size())))
                .orElseThrow(invalidState);
    }

    private static TypeInfo toTypeInfo(TypeInfoParser.TypeVariableContext ctx) {
        return TypeInfo.of(ctx.Identifier().getSymbol().getText());
    }
}
