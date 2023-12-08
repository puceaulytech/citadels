package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class VoleurAbilityActionTest {
    @Test
    void use() throws IllegalActionException {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();
                voleurAction.stealFrom(Role.MAGICIEN);
            }
        };

        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.VOLEUR);
        SelfPlayerView playerView = new SelfPlayerView(player);

        RegularTurnAction action = new RegularTurnAction(playerView, null);
        player.getBehavior().playTurn(action, playerView, null);

        VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();

        assertEquals(Role.MAGICIEN, voleurAction.getStolenRole());
    }

    @Test
    void stealFromAssassin() {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();
                voleurAction.stealFrom(Role.ASSASSIN);
            }
        };

        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.VOLEUR);
        SelfPlayerView playerView = new SelfPlayerView(player);

        RegularTurnAction action = new RegularTurnAction(playerView, null);
        assertThrows(IllegalActionException.class, () -> player.getBehavior().playTurn(action, playerView, null));
    }
}
