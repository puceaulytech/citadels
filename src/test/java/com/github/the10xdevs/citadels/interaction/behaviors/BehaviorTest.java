package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BehaviorTest {
    @Test
    void getName() {
        assertEquals("Ok", new OkBehavior().getName());

        assertEquals("OtherName", new OtherName().getName());
    }

    static class OkBehavior implements Behavior {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        }
    }

    static class OtherName implements Behavior {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        }
    }
}