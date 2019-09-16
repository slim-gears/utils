package com.slimgears.util.autovalue.apt;

import com.google.auto.value.processor.AutoValueProcessor;
import com.slimgears.apt.util.AnnotationProcessingTester;
import com.slimgears.apt.util.StoreWrittenFilesRule;
import org.junit.ClassRule;
import org.junit.Test;

public class AutoGenericAnnotationProcessorTest {
    @ClassRule public final static StoreWrittenFilesRule storeWrittenFilesRule = StoreWrittenFilesRule
            .forPath("build/test-results/files");

    @Test
    public void testGenericClassGeneration() {
        AnnotationProcessingTester.create()
                .inputFiles("SampleAutoGenericClass.java")
                .expectedSources("StringIntegerGenericClass.java", "IntegerStringGenericClass.java")
                .processedWith(new AutoGenericAnnotationProcessor())
                .test();
    }

    @Test
    public void testGenericInterfaceGeneration() {
        AnnotationProcessingTester.create()
                .inputFiles("SampleAutoGenericInterface.java", "CustomAnnotation.java")
                .expectedSources("StringIntegerGenericInterface.java", "IntegerStringGenericInterface.java")
                .processedWith(new AutoGenericAnnotationProcessor())
                .test();
    }

    @Test
    public void testGenericAutoValuePrototypeInterfaceGeneration() {
        AnnotationProcessingTester.create()
                .inputFiles(
                    "SampleAutoGenericAutoValuePrototype.java",
                    "SampleFieldAnnotation.java",
                    "SampleValuePrototype.java")
                .expectedSources(
                    "StringIntegerInterface.java",
                    "StringSampleValueInterface.java")
                .processedWith(new AutoGenericAnnotationProcessor(), new AutoValuePrototypeAnnotationProcessor(), new AutoValueProcessor())
                .test();
    }
}
