package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;

import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class ArchitecteAbilityActionTest {

    private Player currentPlayer;
    private Game game;
    private ArchitecteAbilityAction architecteAction;

    @BeforeEach
    void setUp() {
        Behavior emptyBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        currentPlayer = new Player(null);
        game = new Game(List.of(emptyBehavior, new RandomBehavior()));
        architecteAction = new ArchitecteAbilityAction(currentPlayer, game);
    }

    @Test
    void testDrawAdditionalCards() {
        int initialHandSize = currentPlayer.getHand().size();

        //when
        architecteAction.drawAdditionalCards();

        //then
        int newHandSize = currentPlayer.getHand().size();
        assertEquals(initialHandSize + 3, newHandSize);
    }
    @Test
    void testBuildDistricts() throws IllegalActionException, DuplicatedDistrictException {
        // Given
        District district1 = new District("District1", Category.RELIGIEUX, 3);
        District district2 = new District("District2", Category.MARCHAND, 4);
        District district3 = new District("District3", Category.NOBLE, 5);

        // Set up player's initial hand
        currentPlayer.getHand().add(district1);
        currentPlayer.getHand().add(district2);
        currentPlayer.getHand().add(district3);

        // When
        architecteAction.buildDistricts(List.of(district1, district2, district3));

        // Then
        assertTrue(currentPlayer.getCity().getDistricts().contains(district1));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district2));
        assertTrue(currentPlayer.getCity().getDistricts().contains(district3));

        assertEquals(3, architecteAction.getDistrictsToBuild(), "DistrictsToBuild count should be 3");


        assertEquals(0, currentPlayer.getHand().size(), "hand is empty after");
    }

}