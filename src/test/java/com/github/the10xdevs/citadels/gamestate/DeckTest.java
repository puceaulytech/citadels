package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class DeckTest {
    District romain = new District("Baraque de Romain", Category.MERVEILLE, 10);
    District logan = new District("Baraque de Logan", Category.MILITAIRE, 10);
    District vahan = new District("Baraque de Vahan", Category.NOBLE, 10);
    Deck deck;

    @BeforeEach
    void setup() {
        deck = new Deck(List.of(romain, logan, vahan));
    }

    @Test
    void drawCard() {
        int oldSize = deck.getCardsCount();
        District popped = deck.drawCard();
        assertEquals(oldSize - 1, deck.getCardsCount());
        assertEquals(romain, popped);
    }

    @Test
    void enqueueCard() {
        int oldSize = deck.getCardsCount();
        District test = new District("Testing", Category.MERVEILLE, 4);
        deck.enqueueCard(test);
        assertEquals(oldSize + 1, deck.getCardsCount());
        deck.drawCard();
        deck.drawCard();
        deck.drawCard();
        assertEquals(test, deck.drawCard());
    }

    @Test
    void peekTwo() {
        Pair<District, District> pair = deck.peekFirstTwo();
        assertEquals(romain, pair.first());
        assertEquals(logan, pair.second());
    }
}