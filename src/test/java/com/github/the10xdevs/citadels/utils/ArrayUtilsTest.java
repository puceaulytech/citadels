package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;


class ArrayUtilsTest {

    @Test
    void ArrayUtils() {
        // Utilisez assertThrows pour vérifier qu'une exception est lancée lors de l'instanciation
        assertThrows(IllegalStateException.class, ArrayUtils::new);
    }

    @Test
    void addAmount() {
        List<Integer> example = new ArrayList<>();
        ArrayUtils.addAmount(example, 4, 4);
        assertEquals(4, example.size());
        assertEquals(4, example.stream().filter(i -> i == 4).count());
        assertEquals(0, example.stream().filter(i -> i == 3).count());
        assertEquals(0, example.stream().filter(i -> i == 88).count());

        ArrayUtils.addAmount(example, 3, 2);

        assertEquals(6, example.size());
        assertEquals(4, example.stream().filter(i -> i == 4).count());
        assertEquals(2, example.stream().filter(i -> i == 3).count());
        assertEquals(0, example.stream().filter(i -> i == 88).count());

        ArrayUtils.addAmount(example, 88, 1);

        assertEquals(7, example.size());
        assertEquals(4, example.stream().filter(i -> i == 4).count());
        assertEquals(2, example.stream().filter(i -> i == 3).count());
        assertEquals(1, example.stream().filter(i -> i == 88).count());

    }
}