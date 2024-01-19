package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

class RandomUtilsTest {

    @Test
    void RandomUtils() {
        assertThrows(IllegalStateException.class, ArrayUtils::new);
    }

    @Test
    void chooseFrom() {
        List<Integer> integers = List.of(1, 2, 4, 5);

        int element = RandomUtils.chooseFrom(new Random(), integers);

        assertTrue(integers.contains(element));
    }
}