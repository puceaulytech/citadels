package com.github.the10xdevs.citadels.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

/**
 * Utility class for random operations.
 */

public class RandomUtils {
    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    RandomUtils() {
        throw new IllegalStateException("Utility class");
    }

    /**
     * Chooses a random element from a collection.
     *
     * @param randomGenerator the random number generator to use
     * @param collection      the collection to choose from
     * @param <T>             the type of elements in the collection
     * @return a randomly chosen element from the collection, or null if the collection is empty
     * @throws IllegalStateException if the random position generated is out of bounds
     */

    public static <T> T chooseFrom(Random randomGenerator, Collection<T> collection) {
        if (collection.isEmpty()) return null;

        int pos = randomGenerator.nextInt(collection.size());

        Iterator<T> iterator = collection.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            T element = iterator.next();

            if (i == pos) return element;

            i++;
        }

        throw new IllegalStateException("Out of bound random position");
    }
}
