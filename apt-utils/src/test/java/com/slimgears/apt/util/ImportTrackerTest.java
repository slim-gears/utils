package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import org.junit.Assert;
import org.junit.Test;

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
        importTracker.use(TypeInfo.of("com.slimgears.apt.util.ImportTrackerTest[]"));
        Assert.assertEquals(1, importTracker.imports().length);
        Assert.assertEquals("com.slimgears.apt.util.ImportTrackerTest", importTracker.imports()[0]);
    }
}
