package com.slimgears.util.stream;

import com.google.common.collect.ImmutableMultimap;
import com.google.common.collect.Multimap;
import org.junit.Assert;
import org.junit.Test;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class StreamsTest {
    @Test
    public void testStreamTopologicalSort() {
        Multimap<Integer, Integer> edges = ImmutableMultimap.<Integer, Integer>builder()
                .putAll(5, 3, 8, 1)
                .putAll(8, 2, 1)
                .putAll(2, 3)
                .putAll(4, 5)
                .putAll(6, 4)
                .putAll(7, 5)
                .putAll(1, 2)
                .build();

        List<Integer> orderedList = Streams
                .orderByTopology(IntStream.range(1, 9).boxed(), num -> edges.get(num).stream())
                .collect(Collectors.toList());

        orderedList.forEach(System.out::println);

        Assert.assertEquals(8, orderedList.size());
    }
}
