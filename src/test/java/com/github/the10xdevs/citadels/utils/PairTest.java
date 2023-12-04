package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

class PairTest {
    @Test
    void create() {
        Pair<Integer, Boolean> example = new Pair<>(5, true);
        assertEquals(5, example.first());
        assertTrue(example.second());
    }

    @Test
    void invert() {
        Pair<Integer, Boolean> example = new Pair<>(5, true);
        Pair<Boolean, Integer> other = example.invert();
        assertTrue(other.first());
        assertEquals(5, other.second());
    }
}