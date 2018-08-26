package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.junit.Assert;
import org.junit.Test;

import static com.slimgears.apt.util.TypeInfoParserAdapter.toTypeInfo;

public class AntlrTest {

    @Test
    public void testBasicTypeInfoParsing() {
        TypeInfo type = toTypeInfo("java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>");
        Assert.assertEquals("java.util.Map<java.lang.Integer, java.util.List<java.lang.String>>", type.fullName());
        Assert.assertEquals("java.util.Map", type.name());
        Assert.assertEquals("java.lang.Integer", type.typeParams().get(0).type().fullName());
        Assert.assertEquals("java.util.List", type.typeParams().get(1).type().name());
        Assert.assertEquals("java.util.List<java.lang.String>", type.typeParams().get(1).type().fullName());
        Assert.assertEquals("java.lang.String", type.typeParams().get(1).type().elementTypeOrSelf().name());
    }

    @Test
    public void testWildcardTypeInfoParsing() {
        TypeInfo type = toTypeInfo("java.util.Map<? extends java.lang.Integer, ? extends java.util.List<? super java.lang.String>>");
        Assert.assertEquals("java.util.Map<? extends java.lang.Integer, ? extends java.util.List<? super java.lang.String>>", type.fullName());
    }

}
