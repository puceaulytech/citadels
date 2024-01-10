package com.github.the10xdevs.citadels.utils;

/**
 * A pair of two values
 *
 * @param <A> First value
 * @param <B> Second value
 */
public class Pair<A, B> {
    private final A first;
    private final B second;

    /**
     * Construct a new pair with two initial values
     *
     * @param first  The first initial value
     * @param second The second initial value
     */
    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    /**
     * Get the first value
     *
     * @return The first value
     */
    public A first() {
        return this.first;
    }

    /**
     * Get the second value
     *
     * @return The second value
     */
    public B second() {
        return this.second;
    }

    /**
     * Invert the pair, putting the first value into the second, and vice-versa.
     *
     * @return The inverted pair
     */
    public Pair<B, A> invert() {
        return new Pair<>(this.second, this.first);
    }

    /**
     * Check if the pair contains a specific object
     *
     * @param o The object to check
     * @return true if the pair contains the object, otherwise false
     */
    public boolean contains(Object o) {
        return this.first.equals(o) || this.second.equals(o);
    }

    /**
     * Returns true if the pair contains no elements
     *
     * @return true if the pair contains no elements
     */
    public boolean isEmpty() {
        return this.first == null && this.second == null;
    }
}