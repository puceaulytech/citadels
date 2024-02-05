package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.utils.Pair;

import java.util.*;

/**
 * A deck of cards
 */
public class Deck<E> {
    private final Deque<E> elements = new ArrayDeque<>();

    public Deck() {
    }

    /**
     * Create a new deck with initial values
     *
     * @param cards The initial values, will be inserted in order
     */
    public Deck(Collection<E> cards) {
        this.elements.addAll(cards);
    }

    /**
     * Shuffle the deck
     */
    public void shuffle() {
        List<E> temporary = new ArrayList<>(this.elements);
        Collections.shuffle(temporary);
        this.elements.clear();
        this.elements.addAll(temporary);
    }

    /**
     * Draw a card, removing it from the deck
     *
     * @return The drawn card
     */
    public E drawCard() {
        if (this.elements.isEmpty())
            throw new IllegalStateException("Deck is empty");

        return this.elements.poll();
    }

    /**
     * Put a card at the bottom of the deck
     *
     * @param element The card to put
     */
    public void enqueueCard(E element) {
        this.elements.offer(element);
    }

    /**
     * Get number of cards in the deck
     *
     * @return The number of cards
     */
    public int getCardsCount() {
        return this.elements.size();
    }

    /**
     * Retrieve the first two elements of the deck
     *
     * @return A pair containing the first two elements or some null if the deck has one or no cards
     */
    public Pair<E, Optional<E>> peekFirstTwo() {
        Iterator<E> iterator = this.elements.iterator();
        return new Pair<>(iterator.hasNext() ? iterator.next() : null, iterator.hasNext() ? Optional.of(iterator.next()) : Optional.empty());
    }

    /**
     * Returns true if the deck contains no district cards
     *
     * @return true if the deck contains no district cards
     */
    public boolean isEmpty() {
        return this.elements.isEmpty();
    }

    public boolean remove(E element) {
        return this.elements.remove(element);
    }

    public Deque<E> getElements() {
        return this.elements;
    }
}