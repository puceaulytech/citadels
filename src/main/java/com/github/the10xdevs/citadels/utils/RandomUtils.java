package com.github.the10xdevs.citadels.utils;

import java.util.Collection;
import java.util.Iterator;
import java.util.Random;

public class RandomUtils {
    private static Random randomGenerator = new Random();

    public static <T> T chooseFrom(Collection<T> collection) {
        int pos = RandomUtils.randomGenerator.nextInt(collection.size());

        Iterator<T> iterator = collection.iterator();
        int i = 0;

        while (iterator.hasNext()) {
            T element = iterator.next();

            if (i == pos) return element;

            i++;
        }

        throw new IllegalStateException("Out of bound random position");
    }

    private RandomUtils()  {
        throw new IllegalStateException("Utility class");
    }
}
