package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.junit.Assert;
import org.junit.Test;


public class TypeInfoParserTest {
    @Test
    public void testTypeInfoParser() {
        TypeInfo typeInfo = TypeInfoParserAdapter.toTypeInfo("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>");
        Assert.assertEquals("java.util.List", typeInfo.name());
        Assert.assertEquals("List", typeInfo.simpleName());
        Assert.assertEquals("java.util", typeInfo.packageName());
        Assert.assertEquals("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>", typeInfo.fullName());
    }

    @Test
    public void testTypeInfoParserForConstrainedArgs() {
        TypeInfo typeInfo = TypeInfoParserAdapter.toTypeInfo("class<? extends java.lang.Object>");
        Assert.assertEquals(1, typeInfo.typeParams().size());
    }

    @Test
    public void testImportTracker() {
        ImportTracker importTracker = ImportTracker.create("");
        String simplified = importTracker.use("java.util.List<java.util.Map<java.lang.String, java.util.List<java.lang.String>>>");
        Assert.assertEquals("List<Map<String, List<String>>>", simplified);
        Assert.assertEquals(3, importTracker.imports().length);
    }
}