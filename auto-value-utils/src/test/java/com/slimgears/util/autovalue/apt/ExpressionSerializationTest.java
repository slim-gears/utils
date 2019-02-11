package com.slimgears.util.autovalue.apt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slimgears.util.autovalue.expressions.ObjectExpression;
import com.slimgears.util.autovalue.expressions.internal.ExpressionModule;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ExpressionSerializationTest {
    private ObjectMapper objectMapper;

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper().registerModule(new ExpressionModule());
    }

    @Test
    public void testSerialization() throws IOException {
        ObjectExpression<Integer> intExpression = ObjectExpression.<TestEntity>arg()
                .stringRef(TestEntity.metaClass.text)
                .concat(ObjectExpression.<TestEntity>arg().stringRef(TestEntity.metaClass.description))
                .length()
                .add(5);

        String json = objectMapper.writeValueAsString(intExpression);
        Assert.assertNotNull(json);
        System.out.println(json);

        ObjectExpression<Integer> deserializedExpression = objectMapper.readValue(json, new TypeReference<ObjectExpression<Integer>>(){});
        Assert.assertNotNull(deserializedExpression);
        Assert.assertEquals(intExpression, deserializedExpression);
    }
}
