package com.slimgears.util.generic;

import com.slimgears.apt.util.AnnotationProcessingTester;
import org.junit.Test;

public class AutoGenericAnnotationProcessorTest {
    @Test
    public void testGenericClassGeneration() {
        AnnotationProcessingTester.create()
                .inputFiles("SampleGenericClass.java")
                .expectedSources("StringIntegerGenericClass.java", "IntegerStringGenericClass.java")
                .processedWith(new AutoGenericAnnotationProcessor())
                .test();
    }
}