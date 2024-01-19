package com.github.the10xdevs.citadels.utils;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

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

    @Test
    void contains() {
        Pair<Integer, Integer> test = new Pair<>(5, 2);
        assertTrue(test.contains(2));
        assertTrue(test.contains(5));
        assertFalse(test.contains(0));
        assertFalse(test.contains("test"));
    }
    @Test
    void testPairIsEmpty() {
        Pair<String, Integer> nonEmptyPair = new Pair<>("ZAK", 42);
        assertFalse(nonEmptyPair.isEmpty());

        Pair<String, Integer> emptyPair = new Pair<>(null, null);
        assertTrue(emptyPair.isEmpty());
    }
}