package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {

    @Test
    void pollCard() {
        Deck deck = new Deck();
        int oldSize = deck.getCardsCount();
        District popped = deck.pollCard();
        assertEquals(oldSize - 1, deck.getCardsCount());
        assertNotNull(popped);
    }

    @Test
    void enqueueCard() {
        Deck deck = new Deck();
        int oldSize = deck.getCardsCount();
        deck.enqueueCard(new District("Testing", Category.MERVEILLE, 4));
        assertEquals(oldSize + 1, deck.getCardsCount());
    }
}