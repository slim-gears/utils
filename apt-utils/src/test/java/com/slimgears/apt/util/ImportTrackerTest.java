package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;

public class ImportTrackerTest {
    @Test
    public void testImportComplexType() {
        ImportTracker importTracker = ImportTracker.create();
        importTracker.use(TypeInfo.of("java.util.Map<java.lang.String, java.util.List<com.slimgears.apt.util.ImportTrackerTest[]>>[]"));
        Assert.assertEquals(4, importTracker.imports().length);
        Assert.assertEquals("com.slimgears.apt.util.ImportTrackerTest", importTracker.imports()[0]);
        Assert.assertEquals("java.lang.String", importTracker.imports()[1]);
        Assert.assertEquals("java.util.List", importTracker.imports()[2]);
        Assert.assertEquals("java.util.Map", importTracker.imports()[3]);
    }

    @Test
    public void testArrayImport() {
        ImportTracker importTracker = ImportTracker.create();
        String simplifiedType = importTracker.use(TypeInfo.of("com.slimgears.apt.util.ImportTrackerTest[]"));
        Assert.assertEquals(1, importTracker.imports().length);
        Assert.assertEquals("com.slimgears.apt.util.ImportTrackerTest", importTracker.imports()[0]);
        Assert.assertEquals(simplifiedType, "ImportTrackerTest[]");
    }

    @Test
    public void testGenericArrayImport() {
        ImportTracker importTracker = ImportTracker.create();
        String simplifiedType = importTracker.use(TypeInfo.of("com.slimgears.apt.util.ImportTrackerTest<T>[]"));
        Assert.assertEquals(1, importTracker.imports().length);
        Assert.assertEquals("com.slimgears.apt.util.ImportTrackerTest", importTracker.imports()[0]);
        Assert.assertEquals(simplifiedType, "ImportTrackerTest<T>[]");
    }

    @Test
    public void testGenericBoundClassImport() {
        ImportTracker importTracker = ImportTracker.create();
        String simplifiedType = importTracker.use("T extends java.util.Comparable<T>");
        Assert.assertEquals("T extends Comparable<T>", simplifiedType);
        Assert.assertEquals(1, importTracker.imports().length);
        Assert.assertEquals("java.util.Comparable", importTracker.imports()[0]);
    }

    @Test
    public void testNestedClassImport() {
        ImportTracker importTracker = ImportTracker.create();
        TypeInfo nestedType = TypeInfo.of("java.util.Collection<com.slimgears.apt.util.ImportTrackerTest$NestedClass>");

        String simplifiedName = importTracker.use(nestedType);
        Assert.assertEquals("Collection<ImportTrackerTest.NestedClass>", simplifiedName);
        Assert.assertEquals(2, importTracker.imports().length);
        Assert.assertEquals("com.slimgears.apt.util.ImportTrackerTest", importTracker.imports()[0]);
        Assert.assertEquals(Collection.class.getName(), importTracker.imports()[1]);
    }
}
