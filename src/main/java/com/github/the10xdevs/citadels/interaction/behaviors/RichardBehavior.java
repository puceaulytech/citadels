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

            if (self.equals(winningPlayer) || condottiereTakenByPotentialWinner(gameState)) {

                AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();
                abilityAction.kill(Role.CONDOTTIERE);
            }
            else if (voleurTakenByPotentialWinner(gameState) || shouldKillVoleurForEnrichment(self)) {
                AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();
                abilityAction.kill(Role.VOLEUR);
            }
        }

        super.playTurn(action, self, gameState);
    }
    private boolean condottiereTakenByPotentialWinner(GameView gameState) {
        PlayerView condottierePlayer = getRolePlayer(gameState, Role.CONDOTTIERE);
        return condottierePlayer != null && condottierePlayer.getCity().getDistricts().size() == 8;
    }

    private boolean voleurTakenByPotentialWinner(GameView gameState) {
        PlayerView voleurPlayer = getRolePlayer(gameState, Role.VOLEUR);
        return voleurPlayer != null && voleurPlayer.getCity().getDistricts().size() == 8;
    }

    private PlayerView getRolePlayer(GameView gameState, Role role) {
        return gameState.getPlayers()
                .stream()
                .filter(player -> player.getCurrentRole() == role)
                .findFirst()
                .orElseThrow();
    }
    private boolean shouldKillVoleurForEnrichment(SelfPlayerView self) {
        int playerGold = self.getGold();
        int maxAllowedGold = 10;
        return playerGold > maxAllowedGold;
    }
    

}
