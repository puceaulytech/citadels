package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;

import java.util.*;

/**
 * A deck of cards
 */
public class Deck {
    private final Deque<District> districts = new ArrayDeque<>();

    /**
     * Create a new deck with initial values
     * @param cards The initial values, will be inserted in order
     */
    public Deck(Collection<District> cards) {
        this.districts.addAll(cards);
    }

    /**
     * Shuffle the deck
     */
    public void shuffle() {
        List<District> elements = new ArrayList<>(this.districts);
        Collections.shuffle(elements);
        this.districts.clear();
        this.districts.addAll(elements);
    }

    /**
     * Draw a card, removing it from the deck
     * @return The drawn card
     */
    public District drawCard() {
        return this.districts.poll();
    }

    /**
     * Put a card at the bottom of the deck
     * @param district The card to put
     */
    public void enqueueCard(District district) {
        this.districts.offer(district);
    }

    /**
     * Get number of cards in the deck
     * @return The number of cards
     */
    public int getCardsCount() {
        return this.districts.size();
    }

    /**
     * Retrieve the first two elements of the deck
     * @return A pair containing the first two elements
     */
    public Pair<District, District> peekFirstTwo() {
        Iterator<District> iterator = this.districts.iterator();
        return new Pair<>(iterator.next(), iterator.next());
    }
}
