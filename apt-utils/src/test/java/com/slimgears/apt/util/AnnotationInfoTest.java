package com.slimgears.apt.util;

import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.AnnotationValueInfo;
import com.slimgears.apt.data.TypeInfo;
import org.junit.Assert;
import org.junit.Test;

@interface TestAnnotation {
    Class value() default Object.class;
    Item[] items() default {};
    Item singleItem() default @Item;

    @interface Item {
        String value() default "";
        int[] items() default {};
    }
}

public class AnnotationInfoTest {
    @Test
    public void testAnnotationInfoAsString() {
        AnnotationInfo annotation = AnnotationInfo
                .builder()
                .type(TestAnnotation.class)
                .value(AnnotationValueInfo.ofType("value", TypeInfo.of(getClass())))
                .value(AnnotationValueInfo.ofArray("items", TestAnnotation.Item[].class,
                        AnnotationValueInfo.Value.ofAnnotation(AnnotationInfo
                                .builder()
                                .type(TestAnnotation.Item.class)
                                .value(AnnotationValueInfo.ofPrimitive("value", "TestValue1"))
                                .build()),
                        AnnotationValueInfo.Value.ofAnnotation(AnnotationInfo
                                .builder()
                                .type(TestAnnotation.Item.class)
                                .value(AnnotationValueInfo.ofPrimitive("value", "TestValue2"))
                                .value(AnnotationValueInfo.ofArray("items", int[].class, 1, 2, 3))
                                .build()),
                        AnnotationValueInfo.Value.ofAnnotation(AnnotationInfo
                                .builder()
                                .type(TestAnnotation.Item.class)
                                .value(AnnotationValueInfo.ofPrimitive("value", "TestValue3"))
                                .build())))
                .value(AnnotationValueInfo.ofAnnotation("item", AnnotationInfo
                        .builder()
                        .type(TestAnnotation.Item.class)
                        .build()))
                .build();

        Assert.assertEquals(
                "@TestAnnotation(value = AnnotationInfoTest.class, " +
                "items = {" +
                        "@Item(\"TestValue1\"), " +
                        "@Item(value = \"TestValue2\", items = {1, 2, 3}), " +
                        "@Item(\"TestValue3\")}, " +
                        "item = @Item)", annotation.asString());
    }

    @Test
    public void testBasicAnnotationInfoAsString() {
        String annotation = AnnotationInfo
                .builder()
                .type(TestAnnotation.Item.class)
                .value(AnnotationValueInfo.ofPrimitive("value", "TestValue3"))
                .build()
                .asString();

        Assert.assertEquals("@Item(\"TestValue3\")", annotation);
    }
}
