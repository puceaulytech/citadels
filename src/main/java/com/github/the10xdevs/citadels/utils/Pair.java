package com.github.the10xdevs.citadels.utils;

public class Pair<A, B> {
    private final A first;
    private final B second;

    public Pair(A first, B second) {
        this.first = first;
        this.second = second;
    }

    public A first() {
        return this.first;
    }

    public B second() {
        return this.second;
    }

    public Pair<B, A> invert() {
        return new Pair<>(this.second, this.first);
    }

    public boolean contains(Object o) {
        return this.first.equals(o) || this.second.equals(o);
    }
}
