package com.slimgears.util.autovalue.apt;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.slimgears.util.autovalue.annotations.PropertyMeta;
import com.slimgears.util.autovalue.expressions.ArgumentExpression;
import com.slimgears.util.autovalue.expressions.Expression;
import com.slimgears.util.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;

public class ExpressionSerializationTest {
    private ObjectMapper objectMapper;

    static class TestEntity {
        public final static PropertyMeta<TestEntity, TestEntity, String> Text = PropertyMeta
                .create("text", TypeToken.of(String.class), TestEntity::getText, TestEntity::setText);

        private String text;

        @JsonProperty public String getText() {
            return text;
        }

        @JsonProperty public void setText(String text) {
            this.text = text;
        }
    }

    @Before
    public void setUp() {
        objectMapper = new ObjectMapper();
    }

    @Test
    public void testSerialization() throws IOException {
        Expression<Integer> intExpression = ArgumentExpression
                .of(TypeToken.of(TestEntity.class))
                .stringProperty(TestEntity.Text)
                .length()
                .add(5);

        String json = objectMapper.writeValueAsString(intExpression);
        Assert.assertNotNull(json);
        System.out.println(json);

        //Expression<Integer> deserializedExpression = objectMapper.readValue(json, new TypeReference<Expression<Integer>>(){});
        //Assert.assertNotNull(deserializedExpression);
    }
}
