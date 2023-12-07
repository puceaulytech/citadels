package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RandomUtilsTest {

    @Test
    void chooseFrom() {
        List<Integer> integers = List.of(1, 2, 4, 5);

        int element = RandomUtils.chooseFrom(integers);

        assertTrue(integers.contains(element));
    }
}