package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;

class RoleTurnActionTest {

    @Test
    void use() throws IllegalActionException {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
                action.pick(Role.ARCHITECTE);
                action.discard(Role.MAGICIEN);
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        RoleTurnAction action = new RoleTurnAction();

        testBehavior.pickRole(action, Set.of(Role.ARCHITECTE, Role.MAGICIEN));

        assertEquals(Role.ARCHITECTE, action.getPickedRole());
        assertEquals(Role.MAGICIEN, action.getDiscardedRole());
    }
}