package com.github.the10xdevs.citadels.logging;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;

public class ConsoleLogger {
    /**
     * Log the start of a turn
     * @param turn The turn number
     */
    public void logTurnStart(int turn) {
        System.out.printf("\n------ Tour n°%d ------\n", turn);
    }

    /**
     * Log a role choosing action
     * @param index The index of the player
     * @param action The action made by the player
     */
    public void logRoleTurnAction(int index, RoleTurnAction action) {
        System.out.printf("\n--- Joueur n°%d ---\n", index + 1);
        System.out.printf("Choisit le rôle %s\n", action.getPickedRole());
        if (action.getDiscardedRole() != null)
            System.out.printf("Défausse le rôle %s\n", action.getDiscardedRole());
    }

    /**
     * Log a regular turn action
     * @param player The player that made the action
     * @param action The action made by the player
     */
    public void logRegularTurnAction(Player player, RegularTurnAction action) {
        System.out.printf("\n--- Joueur ayant le rôle %s ---\n", player.getCurrentRole());

        if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
            System.out.println("Prend deux pièces d'or");
        } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
            System.out.println("Pioche deux cartes");
            System.out.printf("    garde    %s,\n", action.getChosenCard());
            System.out.printf("    défausse %s\n", action.getDiscardedCard());
        }

        if (action.getBuiltDistrict() != null) {
            System.out.printf("Construit le quartier %s\n", action.getBuiltDistrict());
        }
    }

    /**
     * Log winners of the game
     * @param players All the players
     */
    public void logWinners(List<Player> players) {
        System.out.println("\n------ Podium ------");
        for (Player player : players) {
            int score = player.getCity().getDistricts().stream().mapToInt(District::getCost).sum();

            System.out.printf("-> %s avec %d points\n", player.getBehavior().getClass().getSimpleName(), score);
        }
    }

    /**
     * Log if an error occurs during the game
     * @param error
     */
    public void logError(Throwable error) {
        System.out.println("\n------ Error ------");
        System.out.println("Something went wrong during play: " + error.getMessage());
    }
}
