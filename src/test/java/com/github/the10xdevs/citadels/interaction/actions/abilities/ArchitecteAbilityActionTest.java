package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class ArchitecteAbilityActionTest {
    private Player currentPlayer;
    private ArchitecteAbilityAction architecteAction;

    District district1 = new District("District1", Category.RELIGIEUX, 3);
    District district2 = new District("District2", Category.MARCHAND, 4);
    District district3 = new District("District3", Category.NOBLE, 5);
    District district4 = new District("District4", Category.MILITAIRE, 5);

    @BeforeEach
    void setUp() {
        Behavior emptyBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        currentPlayer = new Player(null);
        Game game = GameBuilder.create().addBehavior(emptyBehavior).addBehavior(new RandomBehavior()).build();
        architecteAction = new ArchitecteAbilityAction(currentPlayer, game);
    }

    @Test
    void testDrawAdditionalCards() throws IllegalActionException {
        int initialHandSize = currentPlayer.getHand().size();

        //when
        architecteAction.drawAdditionalCards();

        //then
        int newHandSize = currentPlayer.getHand().size();
        assertEquals(initialHandSize + 2, newHandSize);
        assertEquals(2, architecteAction.getDrawnCards().size());
    }

    @Test
    void testDrawAdditionalCardsInEmptyDeck() throws IllegalActionException {
        ArchitecteAbilityAction abilityAction = new ArchitecteAbilityAction(
                currentPlayer,
                GameBuilder.create().withDeck(new Deck<>()).build()
        );

        assertDoesNotThrow(abilityAction::drawAdditionalCards);
        assertTrue(currentPlayer.getHand().isEmpty());
    }

    @Test
    void testDrawAdditionalCardsTwice() throws IllegalActionException {
        //when
        architecteAction.drawAdditionalCards();
        assertThrows(IllegalActionException.class, architecteAction::drawAdditionalCards);
    }

    @Test
    void testBuildDistricts() throws IllegalActionException {
        // Set up player's initial hand
        currentPlayer.setGold(12);
        currentPlayer.getHand().add(district1);
        currentPlayer.getHand().add(district2);
        currentPlayer.getHand().add(district3);

        // When
        assertDoesNotThrow(() -> architecteAction.buildDistricts(List.of(district1, district2, district3)));

        // Then
        assertTrue(currentPlayer.getCity().getDistricts().contains(district1));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district2));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district3));

        assertEquals(0, architecteAction.getRemainingMaxDistricts());
        assertEquals(3, architecteAction.getBuiltDistricts().size());

        assertEquals(0, currentPlayer.getHand().size());
    }

    @Test
    void testBuildDistrictsInMultipleTimes() throws IllegalActionException {
        // Set up player's initial hand
        currentPlayer.setGold(12);
        currentPlayer.getHand().add(district1);
        currentPlayer.getHand().add(district2);
        currentPlayer.getHand().add(district3);

        architecteAction.buildDistricts(List.of(district1, district2));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district1));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district2));

        assertEquals(1, architecteAction.getRemainingMaxDistricts());
        assertDoesNotThrow(() -> architecteAction.buildDistricts(List.of(district3)));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district3));

        assertEquals(3, architecteAction.getBuiltDistricts().size());
        assertEquals(0, currentPlayer.getHand().size());
    }

    @Test
    void testBuildTooManyDistricts() {
        // Set up player's initial hand
        currentPlayer.setGold(20);
        currentPlayer.getHand().add(district1);
        currentPlayer.getHand().add(district2);
        currentPlayer.getHand().add(district3);
        currentPlayer.getHand().add(district4);

        // When
        assertThrows(IllegalActionException.class, () -> architecteAction.buildDistricts(List.of(district1, district2, district3, district4)));
    }
}