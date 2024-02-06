package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.ExpensiveBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.logging.VoidLogger;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class GameTest {
    Behavior emptyBehavior = new Behavior() {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) {
        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
        }
    };

    @Test
    void isGameOver() throws DuplicatedDistrictException {
        List<District> districts = new ArrayList<>(new HashSet<>(District.all()));
        Game game = GameBuilder.create().addBehavior(emptyBehavior).addBehavior(emptyBehavior).build();
        game.getPlayers().get(0).getCity().addDistrict(districts.get(0));
        assertFalse(game.isGameOver());
        for (int i = 0; i < 8; i++) {
            game.getPlayers().get(1).getCity().addDistrict(districts.get(i));
        }
        assertTrue(game.isGameOver());
    }

    @Test
    void gameGoingWell() {
        Game game = GameBuilder.create()
                .addBehavior(new RandomBehavior())
                .addBehavior(new RandomBehavior())
                .addBehavior(new FastBuilderBehavior())
                .addBehavior(new ExpensiveBuilderBehavior())
                .withLogger(new VoidLogger())
                .build();

        assertDoesNotThrow(game::start);
        assertTrue(game.isGameOver());
    }

    @Test
    void shouldConstructWithMessageOnly() {
        IllegalActionException exception = new IllegalActionException("Test message");
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldConstructWithMessageAndCause() {
        Throwable cause = new RuntimeException("Test cause");
        IllegalActionException exception = new IllegalActionException("Test message", cause);
        assertEquals("Test message", exception.getMessage());
        assertEquals(cause, exception.getCause());
    }

    @Test
    void shouldConstructWithNullMessage() {
        IllegalActionException exception = new IllegalActionException(null);
        assertNull(exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void shouldConstructWithNullCause() {
        IllegalActionException exception = new IllegalActionException("Test message", null);
        assertEquals("Test message", exception.getMessage());
        assertNull(exception.getCause());
    }
}
