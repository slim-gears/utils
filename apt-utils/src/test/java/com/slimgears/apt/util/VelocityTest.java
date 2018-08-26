package com.slimgears.apt.util;

import com.google.common.collect.ImmutableMap;
import com.google.escapevelocity.Template;
import org.junit.Assert;
import org.junit.Test;

import java.io.IOException;
import java.io.StringReader;

public class VelocityTest {
    private static final String template = "Foo value: $foo, Bar value: $bar";

    @Test
    public void testSimpleTemplate() throws IOException {
        ImmutableMap<String, Object> vars = ImmutableMap.<String, Object>builder()
                .put("foo", "fooVal")
                .put("bar", "barVal")
                .build();
        Template template = Template.parseFrom(new StringReader(VelocityTest.template));
        String result = template.evaluate(vars);
        Assert.assertEquals("Foo value: fooVal, Bar value: barVal", result);
    }

    @Test
    public void testTypeScriptTemplate() {
    }
}
