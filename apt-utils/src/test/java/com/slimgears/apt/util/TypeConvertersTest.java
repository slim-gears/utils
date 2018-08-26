/**
 *
 */
package com.slimgears.rxrpc.apt;

import com.slimgears.rxrpc.apt.data.TypeInfo;
import com.slimgears.rxrpc.apt.util.TypeConverter;
import com.slimgears.rxrpc.apt.util.TypeConverters;
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

    private void testConversion(TypeInfo from, TypeInfo expected) {
        Assert.assertTrue(propertiesTypeConverter.canConvert(from));
        TypeInfo actual = propertiesTypeConverter.convert(propertiesTypeConverter, from);
        Assert.assertEquals(expected, actual);
    }
}
