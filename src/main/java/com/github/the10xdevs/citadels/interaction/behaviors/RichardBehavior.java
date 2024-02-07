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
import java.util.Optional;

public class RichardBehavior extends FastBuilderBehavior {
    private static final int RICH_THRESHOLD = 10;

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        // TODO: richard magic tricks
        
        super.pickRole(action, self, gameState);
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        if (self.getCurrentRole() == Role.ASSASSIN) {
            PlayerView winningPlayer = gameState.getPlayers()
                    .stream()
                    .max(Comparator.comparingInt(PlayerView::getScore))
                    .orElseThrow();

            if (self.equals(winningPlayer) || this.roleTakenByPotentialWinner(Role.CONDOTTIERE, gameState)) {
                AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();
                abilityAction.kill(Role.CONDOTTIERE);
            } else if (this.roleTakenByPotentialWinner(Role.VOLEUR, gameState) || self.getGold() > RICH_THRESHOLD) {
                AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();
                abilityAction.kill(Role.VOLEUR);
            }
        }

        super.playTurn(action, self, gameState);
    }

    private boolean roleTakenByPotentialWinner(Role targetRole, GameView gameState) {
        Optional<PlayerView> playerHavingRole = this.getPlayerWithRole(gameState, targetRole);
        return playerHavingRole.isPresent() && playerHavingRole.get().getCity().getDistricts().size() == 7;
    }

    private Optional<PlayerView> getPlayerWithRole(GameView gameState, Role role) {
        return gameState.getPlayers()
                .stream()
                .filter(player -> player.getCurrentRole() == role)
                .findFirst();
    }
}
