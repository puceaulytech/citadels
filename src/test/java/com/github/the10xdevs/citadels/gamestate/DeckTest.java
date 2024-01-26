package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class DeckTest {
    District romain = new District("Baraque de Romain", Category.MERVEILLE, 10);
    District logan = new District("Baraque de Logan", Category.MILITAIRE, 10);
    District vahan = new District("Baraque de Vahan", Category.NOBLE, 10);
    Deck<District> deck;

    @BeforeEach
    void setup() {
        deck = new Deck<>(List.of(romain, logan, vahan));
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
        Pair<District, Optional<District>> pair = deck.peekFirstTwo();
        assertEquals(romain, pair.first());
        assertEquals(logan, pair.second().orElseThrow());
    }

    @Test
    void drawWhenEmpty() {
        Deck<District> deck = new Deck<>();
        assertThrows(IllegalStateException.class, deck::drawCard);
    }

    @Test
    void pickTwoWhenAlmostEmpty() {
        Deck<District> deck = new Deck<>(List.of(new District("Baraque", Category.MERVEILLE, 10)));
        Pair<District, Optional<District>> pair = deck.peekFirstTwo();
        assertNotNull(pair.first());
        assertTrue(pair.second().isEmpty());
    }
    @Test
    void shuffle() {
        Deck<District> deck = new Deck<>(List.of(
                new District("Card1", Category.MERVEILLE, 5),
                new District("Card2", Category.MILITAIRE, 3),
                new District("Card3", Category.NOBLE, 8)
        ));

        // Version of the deck that will not be shuffled
        Deck<District> nonShuffledDeck = new Deck<>(deck.getElements());

        int i;
        for (i = 0; deck.getElements().equals(nonShuffledDeck.getElements()) && i <= 10000; i++) {
            deck.shuffle();
        }
        // It is assumed that when shuffling the deck 10000 times, it will be in a different state than originally
        assertTrue(i < 10000);
        // And testing if there are still the same cards
        for (District d : nonShuffledDeck.getElements()) {
            assertTrue(deck.getElements().contains(d));
        }
    }

    @Test
    void isEmpty() {
        Deck<District> emptyDeck = new Deck<>();
        assertTrue(emptyDeck.isEmpty());

        Deck<District> nonEmptyDeck = new Deck<>(List.of(new District("Card", Category.MERVEILLE, 5)));
        assertFalse(nonEmptyDeck.isEmpty());
    }

    @Test
    void remove() {
        // Create a deck with known cards
        Deck<District> deck = new Deck<>(List.of(
                new District("Card1", Category.MERVEILLE, 5),
                new District("Card2", Category.MILITAIRE, 3),
                new District("Card3", Category.NOBLE, 8)
        ));

        // Remove a card from the deck
        boolean removed = deck.remove(deck.peekFirstTwo().first());

        // Assert that the removal was successful
        assertTrue(removed);

        // Assert that the deck size has decreased
        assertEquals(2, deck.getCardsCount());
    }

    @Test
    void getElements() {
        // Create a deck with known cards
        Deck<District> deck = new Deck<>(List.of(
                new District("Card1", Category.MERVEILLE, 5),
                new District("Card2", Category.MILITAIRE, 3),
                new District("Card3", Category.NOBLE, 8)
        ));

        // Get the elements from the deck
        List<District> elements = List.copyOf(deck.getElements());

        // Assert that the retrieved elements match the original order
        assertEquals(3, elements.size());
        assertEquals("Card1", elements.get(0).getName());
        assertEquals("Card2", elements.get(1).getName());
        assertEquals("Card3", elements.get(2).getName());
    }
}