package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import java.util.Arrays;
import java.util.List;
import java.util.Random;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilsTest {

    @Test
    void RandomUtils() {
        assertThrows(IllegalStateException.class, RandomUtils::new);
    }

    @Test
    void chooseFrom() {
        List<Integer> integers = List.of(1, 2, 4, 5);

        Integer element = RandomUtils.chooseFrom(new Random(), integers);

        assertTrue(integers.contains(element));
    }
    @Test
    void testChooseFromThrowsException() {
        Random faultyRandomGenerator = new Random() {
            @Override
            public int nextInt(int bound) {
                return bound; // This will be out of bounds for any list
            }
        };

        List<Integer> list = Arrays.asList(1, 2, 3, 4, 5);

        assertThrows(IllegalStateException.class, () -> RandomUtils.chooseFrom(faultyRandomGenerator, list));
    }
}