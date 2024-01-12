package com.github.the10xdevs.citadels.utils;

import java.util.List;

public class ArrayUtils {
    /**
     * Add an element multiple times into a list
     *
     * @param dest    The list
     * @param element The element
     * @param amount  The amount of time to add
     */
    public static <T> void addAmount(List<T> dest, T element, int amount) {
        for (int i = 0; i < amount; i++) {
            dest.add(element);
        }
    }
}
