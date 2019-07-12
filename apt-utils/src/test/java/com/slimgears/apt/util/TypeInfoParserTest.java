package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.junit.Assert;
import org.junit.Test;


public class TypeInfoParserTest {
    static class Nested1 {
        static class Nested2 {
            static class Nested3 {

            }
        }
    }

    @Test
    public void testElementType() {
        TypeInfo typeInfo = TypeInfo.of("java.util.List<java.lang.String[]>[][][]");
        Assert.assertEquals("java.util.List<java.lang.String[]>", typeInfo.elementTypeOrSelf().fullName());
        Assert.assertEquals("java.lang.String[]", typeInfo.elementTypeOrSelf().elementTypeOrSelf().fullName());
        Assert.assertEquals("java.lang.String", typeInfo.elementTypeOrSelf().elementTypeOrSelf().elementTypeOrSelf().fullName());
    }

    @Test
    public void testTypeInfoArrayParser() {
        TypeInfo typeInfo = TypeTokenParserAdapter.toTypeInfo("java.util.List<java.lang.String[]>[]");
        Assert.assertEquals("java.lang.String[]", TypeInfo.of("java.lang.String[]").fullName());

        Assert.assertEquals("java.util.List<java.lang.String[]>[]", typeInfo.fullName());
        Assert.assertEquals("java.util.List[]", typeInfo.erasureName());
        Assert.assertEquals("List[]", typeInfo.simpleName());
        Assert.assertEquals("java.util", typeInfo.packageName());
        Assert.assertEquals("java.util.List<java.lang.String[]>", typeInfo.elementTypeOrSelf().fullName());
        Assert.assertEquals("java.lang.String[]", typeInfo.elementTypeOrSelf().elementTypeOrSelf().fullName());
        Assert.assertEquals("java.lang.String", typeInfo.elementTypeOrSelf().elementTypeOrSelf().elementTypeOrSelf().fullName());
    }

    @Test
    public void testTypeInfoParser() {
        TypeInfo typeInfo = TypeTokenParserAdapter.toTypeInfo("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>");
        Assert.assertEquals("java.util.List", typeInfo.name());
        Assert.assertEquals("List", typeInfo.simpleName());
        Assert.assertEquals("java.util", typeInfo.packageName());
        Assert.assertEquals("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>", typeInfo.fullName());
    }

    @Test
    public void testParsingUnrecognized() {
        Assert.assertEquals("{[key: string]: number[]}", TypeInfo.of("{[key: string]: number[]}").name());
    }

    @Test
    public void testTypeInfoParserForConstrainedArgs() {
        TypeInfo typeInfo = TypeTokenParserAdapter.toTypeInfo("class<? extends java.lang.Object>");
        Assert.assertEquals(1, typeInfo.typeParams().size());
    }

    @Test
    public void testImportTracker() {
        ImportTracker importTracker = ImportTracker.create("");
        String simplified = importTracker.use("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>");
        Assert.assertEquals("List<Map<String, List<String>>>", simplified);
        Assert.assertEquals(3, importTracker.imports().length);
    }

    @SuppressWarnings("ConstantConditions")
    @Test
    public void testNestedTypeParsing() {
        TypeInfo typeInfoWithParam = TypeInfo.of("java.util.List<com.slimgears.apt.util.TypeInfoParserTest$NestedType>");
        Assert.assertTrue(typeInfoWithParam.typeParams().get(0).type().hasEnclosingType());
        TypeInfo typeInfoWithParam2 = TypeInfo.of("java.util.List<com.slimgears.apt.util.TypeInfoParserTest$NestedType$NestedType2>");
        Assert.assertEquals("com.slimgears.apt.util.TypeInfoParserTest$NestedType", typeInfoWithParam2.typeParams().get(0).type().enclosingType().name());

        TypeInfo nested3Type = TypeInfo.of(Nested1.Nested2.Nested3.class);
        Assert.assertEquals(Nested1.Nested2.Nested3.class.getName(), nested3Type.name());

        TypeInfo nested2Type = TypeInfo.of(Nested1.Nested2.class);
        Assert.assertEquals(nested2Type, nested3Type.enclosingType());

    }
}