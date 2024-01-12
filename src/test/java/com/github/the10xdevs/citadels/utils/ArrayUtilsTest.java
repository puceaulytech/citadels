package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ArrayUtilsTest {

    @Test
    void addAmount() {
        List<Integer> example = new ArrayList<>();
        ArrayUtils.addAmount(example, 4, 4);
        ArrayUtils.addAmount(example, 3, 2);
        ArrayUtils.addAmount(example, 88, 1);

        assertEquals(4, example.stream().filter(i -> i == 4).count());
        assertEquals(2, example.stream().filter(i -> i == 3).count());
        assertEquals(1, example.stream().filter(i -> i == 88).count());
    }
}