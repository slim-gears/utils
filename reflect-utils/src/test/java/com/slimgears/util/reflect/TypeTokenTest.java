package com.slimgears.util.reflect;

import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.Map;

public class TypeTokenTest {
    @Test
    public void testParameterizedTypeToken() {
        TypeToken<Map<String, List<Integer>>> token1 = new TypeToken<Map<String, List<Integer>>>() {};

        TypeToken<Map<String, List<Integer>>> token2 = new TypeToken<Map<String, List<Integer>>>() {};
        Assert.assertEquals(token1, token2);

        TypeToken<Map<String, List<Double>>> token3 = new TypeToken<Map<String, List<Double>>>() {};
        Assert.assertNotEquals(token1, token3);

        TypeToken<Map<String, List<Integer>>> token4 = TypeToken
                .ofParameterized(Map.class, TypeToken.of(String.class), new TypeToken<List<Integer>>(){});
        Assert.assertEquals(token1, token4);

        TypeToken<Map<String, List<Integer>>> token5 = TypeToken
                .ofParameterized(Map.class, TypeToken.of(String.class), TypeToken.ofParameterized(List.class, Integer.class));
        Assert.assertEquals(token1, token5);
    }

    @Test
    public void testGenericArrayToken() {
        TypeToken<String[]> strArrayToken1 = new TypeToken<String[]>() {};
        TypeToken<String[]> strArrayToken2 = TypeToken.ofArray(String.class);
        TypeToken<Integer[]> intArray = TypeToken.ofArray(Integer.class);
        Assert.assertEquals(strArrayToken1, strArrayToken2);
        Assert.assertNotEquals(strArrayToken1, intArray);

        TypeToken<String[][]> str2DimArray1 = new TypeToken<String[][]>(){};
        TypeToken<String[][]> str2DimArray2 = TypeToken.ofArray(String[].class);
        Assert.assertNotEquals(strArrayToken1, str2DimArray1);
        Assert.assertEquals(str2DimArray1, str2DimArray2);

        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken1 = new TypeToken<Map<String, List<Integer[]>[]>[]>() {};
        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken2 = TypeToken
                .ofArray(TypeToken.ofParameterized(Map.class,
                        TypeToken.of(String.class),
                        TypeToken.ofArray(TypeToken.ofParameterized(List.class, TypeToken.ofArray(Integer.class)))));

        Assert.assertEquals(mapToken1, mapToken2);
    }
}