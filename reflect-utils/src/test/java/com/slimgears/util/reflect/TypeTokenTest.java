package com.slimgears.util.reflect;

import com.google.common.collect.ImmutableList;
import com.google.common.reflect.TypeToken;
import org.junit.Assert;
import org.junit.Test;

import java.util.Collection;
import java.util.List;
import java.util.Map;

@SuppressWarnings("UnstableApiUsage")
public class TypeTokenTest {
    static class Dummy<T extends Comparable<T>> {
        private final TypeToken<Dummy<T>> token = new TypeToken<Dummy<T>>(){};
        public TypeToken<Dummy<T>> typeToken() {
            return token;
        }
    }

    @Test
    public void testParameterizedTypeToken() {
        TypeToken<Map<String, List<Integer>>> token1 = new TypeToken<Map<String, List<Integer>>>() {};

        TypeToken<Map<String, List<Integer>>> token2 = new TypeToken<Map<String, List<Integer>>>() {};
        Assert.assertEquals(token1, token2);

        TypeToken<Map<String, List<Double>>> token3 = new TypeToken<Map<String, List<Double>>>() {};
        Assert.assertNotEquals(token1, token3);

        TypeToken<Map<String, List<Integer>>> token4 = TypeTokens
                .ofParameterized(Map.class, TypeToken.of(String.class), new TypeToken<List<Integer>>(){});
        Assert.assertEquals(token1, token4);

        TypeToken<Map<String, List<Integer>>> token5 = TypeTokens
                .ofParameterized(Map.class, TypeToken.of(String.class), TypeTokens.ofParameterized(List.class, Integer.class));
        Assert.assertEquals(token1, token5);
    }

    @Test
    public void testGenericArrayToken() {
        TypeToken<String[]> strArrayToken1 = new TypeToken<String[]>() {};
        TypeToken<String[]> strArrayToken2 = TypeTokens.ofArray(String.class);
        TypeToken<Integer[]> intArray = TypeTokens.ofArray(Integer.class);
        Assert.assertEquals(strArrayToken1, strArrayToken2);
        Assert.assertNotEquals(strArrayToken1, intArray);

        TypeToken<String[][]> str2DimArray1 = new TypeToken<String[][]>(){};
        TypeToken<String[][]> str2DimArray2 = TypeTokens.ofArray(String[].class);
        Assert.assertNotEquals(strArrayToken1, str2DimArray1);
        Assert.assertEquals(str2DimArray1, str2DimArray2);

        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken1 = new TypeToken<Map<String, List<Integer[]>[]>[]>() {};
        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken2 = TypeTokens
                .ofArray(TypeTokens.ofParameterized(Map.class,
                        TypeToken.of(String.class),
                        TypeTokens.ofArray(TypeTokens.ofParameterized(List.class, TypeTokens.ofArray(Integer.class)))));

        Assert.assertEquals(mapToken1, mapToken2);
    }

    @SuppressWarnings("AssertEqualsBetweenInconvertibleTypes")
    @Test
    public void testTokenEquality() {
        Assert.assertEquals(new TypeToken<List<?>>(){}, new TypeToken<List<?>>(){});
    }

    @Test
    public void testTokenToString() {
        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken1 = new TypeToken<Map<String, List<Integer[]>[]>[]>() {};
        Assert.assertEquals("java.util.Map<java.lang.String, java.util.List<java.lang.Integer[]>[]>[]", mapToken1.getType().getTypeName());
        Assert.assertEquals("java.util.List<? extends java.lang.Comparable<?>>", new TypeToken<List<? extends Comparable<?>>>(){}.getType().getTypeName());
    }

    @Test
    public void testTokenFromString() {
        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken1 = new TypeToken<Map<String, List<Integer[]>[]>[]>() {};
        TypeToken<Map<String, List<Integer[]>[]>[]> mapToken2 = TypeTokens.valueOf("java.util.Map<java.lang.String, java.util.List<java.lang.Integer[]>[]>[]");
        Assert.assertEquals(mapToken1, mapToken2);
    }

    @Test
    public void testEliminateTypeVars() {
        Dummy<?> dummy = new Dummy<>();
        Assert.assertEquals(
                new TypeToken<Dummy<? extends Comparable<?>>>(){},
                TypeTokens.eliminateTypeVars(dummy.typeToken()));
    }

    @Test
    public void testResolveTypeArgument() {
        TypeToken<ImmutableList<String>> token = new TypeToken<ImmutableList<String>>() {};
        Assert.assertEquals(String.class, token.resolveType(Collection.class.getTypeParameters()[0]).getType());
    }

    @Test
    public void testHasTypeVariables() {
        Assert.assertTrue(TypeTokens.hasTypeVars(TypeToken.of(Dummy.class)));
        Assert.assertFalse(TypeTokens.hasTypeVars(new TypeToken<Dummy<String>>() {}));
    }
}
