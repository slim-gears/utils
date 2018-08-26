/**
 *
 */
package com.slimgears.apt.util;

import org.junit.Assert;
import org.junit.Test;

public class UtilsTest {
    @Test
    public void testCamelCaseToDash() {
        Assert.assertEquals("sample-request", TemplateUtils.camelCaseToDash("SampleRequest"));
    }
}
