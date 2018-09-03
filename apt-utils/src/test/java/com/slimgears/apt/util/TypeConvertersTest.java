/**
 *
 */
package com.slimgears.apt.util;

import com.slimgears.apt.data.TypeInfo;
import com.slimgears.util.guice.ConfigProviders;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Properties;

public class TypeConvertersTest {
    private final static Properties properties = ConfigProviders.create(
            p -> p.put("java.lang.Integer", "number"),
            p -> p.put("java.lang.String", "string"),
            p -> p.put("java.util.Map<K,V>", "Map<${K},${V}>"),
            p -> p.put("java.util.List<T>", "${T}[]"),
            p -> p.put("java.util.Optional<T>", "${T}"),
            p -> p.put("T[]", "${T}[]"));

    private final static TypeConverter propertiesTypeConverter = TypeConverters.fromProperties(properties);

    @Test
    public void testArrayAndListConverterFromProperties() {
        Properties properties = ConfigProviders.create(
                p -> p.put("java.lang.String", "string"),
                p -> p.put("T[]", "${T}[]"));
        TypeConverter converter = TypeConverters.fromProperties(properties);

        testConversion(converter,
                TypeInfo.of(String[].class),
                TypeInfo.of("string[]"));
    }

    @Test
    public void testTypeConverterFromProperties() {
        testConversion(
                TypeInfo.of(Map.class.getName(), TypeInfo.of(String.class), TypeInfo.of(Integer.class)),
                TypeInfo.of("Map<string, number>"));

        testConversion(
                TypeInfo.of(List.class.getName(), TypeInfo.of(String.class)),
                TypeInfo.of("string[]"));

        testConversion(
                TypeInfo.of(String[].class),
                TypeInfo.of("string[]"));

        testConversion(
                TypeInfo.of(Optional.class.getName(), TypeInfo.of(String.class)),
                TypeInfo.of("string"));
    }

    @Test
    public void testTypeConverterWithHardName() {
        Properties properties = new Properties();
        properties.put("java.lang.Integer", "number");
        properties.put("java.lang.String", "string");
        properties.put("java.util.List<T>", "${T}[]");
        properties.put("java.util.Map<K, V>", "`{[key: ${K}]: ${V}}`");
        TypeConverter typeConverter = TypeConverters.fromProperties(properties);
        testConversion(
                typeConverter,
                TypeInfo.of("java.util.Map<java.lang.String, java.util.List<java.lang.Integer>>"),
                TypeInfo.builder().name("{[key: string]: number[]}").build());
    }

    private void testConversion(TypeInfo from, TypeInfo expected) {
        testConversion(propertiesTypeConverter, from, expected);
    }

    private void testConversion(TypeConverter typeConverter, TypeInfo from, TypeInfo expected) {
        Assert.assertTrue(typeConverter.canConvert(from));
        TypeInfo actual = typeConverter.convert(typeConverter, from);
        Assert.assertEquals(expected, actual);
    }
}
