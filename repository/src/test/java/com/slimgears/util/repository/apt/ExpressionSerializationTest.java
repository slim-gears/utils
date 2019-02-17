package com.slimgears.util.repository.apt;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slimgears.util.repository.expressions.BooleanExpression;
import com.slimgears.util.repository.expressions.ObjectExpression;
import com.slimgears.util.repository.expressions.internal.ExpressionModule;
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
        ObjectExpression<TestEntity, Integer> intExpression =
                TestEntity.$.text
                .concat(TestEntity.$.description)
                .length()
                .add(5);

        String json = objectMapper.writeValueAsString(intExpression);
        Assert.assertNotNull(json);
        System.out.println(json);

        ObjectExpression<TestEntity, Integer> deserializedExpression = objectMapper.readValue(json, new TypeReference<ObjectExpression<TestEntity, Integer>>(){});
        Assert.assertNotNull(deserializedExpression);
        Assert.assertEquals(intExpression, deserializedExpression);

        BooleanExpression<TestEntity> expression = BooleanExpression.and(
                TestEntity.$.text.eq("5"),
                TestEntity.$.referencedEntity.description.eq("22"),
                TestEntity.$.referencedEntity.text.startsWith("3"));

        System.out.println(objectMapper.writeValueAsString(expression));
    }
}
