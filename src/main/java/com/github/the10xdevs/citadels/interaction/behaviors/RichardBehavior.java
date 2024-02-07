package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Comparator;
import java.util.Optional;

public class RichardBehavior extends FastBuilderBehavior {
    private boolean needsToKillArchitecture = false;

   // the first player made an enlightened choice during role selection
    private boolean isEnlightenedPlayer = false;


    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        if (!isEnlightenedPlayer) {
            if (action.getAvailableRoles().contains(Role.ASSASSIN)) {
                Optional<PlayerView> futureArchitectePlayer = gameState.getPlayers()
                        .stream()
                        .filter(player -> player.getGold() >= 4)
                        .filter(player -> player.getHandSize() >= 1)
                        .filter(player -> player.getCity().getDistricts().size() >= 5)
                        .findAny();

                if (futureArchitectePlayer.isPresent()) {
                    this.needsToKillArchitecture = true;
                    action.pick(Role.ASSASSIN);
                    return;
                } else if (action.getAvailableRoles().contains(Role.ARCHITECTE)) {
                    // Si l'Assassin n'est pas disponible, le premier joueur  prende l'Architecte
                    action.pick(Role.ARCHITECTE);
                    this.needsToKillArchitecture = true;
                    isEnlightenedPlayer = true;
                    return;
                }
            }

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
        } else if (self.getCurrentRole() == Role.ARCHITECTE) {
            int totalCost = calculateTotalConstructionCost(action.getBuiltDistrict());
            if (totalCost >= 6) {
                //  construire pas les quartiers si le coût total est de 6 pièces d'or ou plus
                return;
            }

        }
        super.playTurn(action, self, gameState);
    }
    private int calculateTotalConstructionCost(District districtToBuild) {
        return districtToBuild.getCost();
    }
}

