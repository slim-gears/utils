package com.slimgears.apt.util;

import com.slimgears.apt.data.AnnotationInfo;
import com.slimgears.apt.data.AnnotationValueInfo;
import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.stream.Comparables;
import org.junit.Assert;
import org.junit.Test;

import static org.hamcrest.CoreMatchers.equalTo;

public class AnnotationInfoTest {
    @interface TestAnnotationWithoutParams {

    }

    @interface TestAnnotation {
        Class value() default Object.class;
        Item[] items() default {};
        Item singleItem() default @Item;
        Item[] emptyItems() default {};
        Class<? extends String> classWithWildcard() default String.class;

        @interface Item {
            String value() default "";
            int[] items() default {};
        }
    }

    @Test
    public void testAnnotationInfoAsString() {
        AnnotationInfo annotation = AnnotationInfo
                .builder()
                .type(TestAnnotation.class)
                .value(AnnotationValueInfo.ofType("value", TypeInfo.of(getClass())))
                .value(AnnotationValueInfo.ofType("classWithWildcard", TypeInfo.of(String.class)))
                .value(AnnotationValueInfo.ofArray("emptyItems", TestAnnotation.Item[].class))
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
                "@AnnotationInfoTest.TestAnnotation(value = AnnotationInfoTest.class, " +
                        "classWithWildcard = String.class, " +
                        "emptyItems = {}, " +
                        "items = {" +
                        "@AnnotationInfoTest.TestAnnotation.Item(\"TestValue1\"), " +
                        "@AnnotationInfoTest.TestAnnotation.Item(value = \"TestValue2\", items = {1, 2, 3}), " +
                        "@AnnotationInfoTest.TestAnnotation.Item(\"TestValue3\")}, " +
                        "item = @AnnotationInfoTest.TestAnnotation.Item)",
                annotation.asString());
    }

    @Test
    public void testBasicAnnotationInfoAsString() {
        String annotation = AnnotationInfo
                .builder()
                .type(TestAnnotation.Item.class)
                .value(AnnotationValueInfo.ofPrimitive("value", "TestValue3"))
                .build()
                .asString();

        Assert.assertEquals("@AnnotationInfoTest.TestAnnotation.Item(\"TestValue3\")", annotation);
    }

    @Test
    public void testAnnotationWithoutParamsToString() {
        String annotation = AnnotationInfo
                .builder()
                .type(TestAnnotationWithoutParams.class)
                .build()
                .asString();

        Assert.assertThat(annotation, equalTo("@AnnotationInfoTest.TestAnnotationWithoutParams"));
    }
}
