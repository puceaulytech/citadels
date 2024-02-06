package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Comparator;

public class RichardBehavior extends FastBuilderBehavior {
    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        // TODO: richard magic tricks
        
        super.pickRole(action, self, gameState);
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        if (self.getCurrentRole() == Role.ASSASSIN) {
            // If we are leading, kill the warlord

            PlayerView winningPlayer = gameState.getPlayers()
                    .stream()
                    .max(Comparator.comparingInt(PlayerView::getScore))
                    .orElseThrow();

            if (self.equals(winningPlayer)) {
                AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();
                abilityAction.kill(Role.CONDOTTIERE);
            }
        }

        super.playTurn(action, self, gameState);
    }
}
