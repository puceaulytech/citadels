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
    private boolean needsToKillArchitecture = false;

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        if (action.getAvailableRoles().contains(Role.ASSASSIN)) {
            Optional<PlayerView> futureArchitectePlayer = gameState.getPlayers()
                    .stream()
                    .filter(player -> player.getGold() >= 4)
                    .filter(player -> player.getHandSize() >= 1)
                    .filter(player -> player.getCity().getDistricts().size() >= 5)
                    .findAny();

            if (futureArchitectePlayer.isPresent()) {
                action.pick(Role.ASSASSIN);
                this.needsToKillArchitecture = true;
            }
        } else {
            super.pickRole(action, self, gameState);
            this.needsToKillArchitecture = false;
        }
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        if (self.getCurrentRole() == Role.ASSASSIN) {
            AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();

            if (this.needsToKillArchitecture) {
                abilityAction.kill(Role.ARCHITECTE);
            } else {
                PlayerView winningPlayer = gameState.getPlayers()
                        .stream()
                        .max(Comparator.comparingInt(PlayerView::getScore))
                        .orElseThrow();

                if (self.equals(winningPlayer)) {
                    abilityAction.kill(Role.CONDOTTIERE);
                }
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
