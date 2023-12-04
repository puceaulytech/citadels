package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegularTurnActionTest {

    @Test
    void takeGold() throws IllegalActionException {
        Behavior goldPickerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
            }
        };

        RegularTurnAction action = new RegularTurnAction(null);

        goldPickerBehavior.playTurn(action, null, null);

        assertEquals(RegularTurnAction.BasicAction.GOLD, action.getBasicAction());
    }

    @Test
    void takeTooMuchGold() throws IllegalActionException {
        Behavior goldPickerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
                action.takeGold();
            }
        };

        RegularTurnAction action = new RegularTurnAction(null);

        assertThrows(IllegalActionException.class, () -> goldPickerBehavior.playTurn(action, null, null));
    }
}