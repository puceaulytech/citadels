package com.github.the10xdevs.citadels.logging;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;

public class ConsoleLogger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";

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
        System.out.printf("Choisit le rôle %s\n", action.getPickedRole().toColorizedString());
        if (action.getDiscardedRole() != null)
            System.out.printf("Défausse le rôle %s\n", action.getDiscardedRole().toColorizedString());
    }

    /**
     * Log a regular turn action
     * @param player The player that made the action
     * @param action The action made by the player
     */
    public void logRegularTurnAction(Player player, RegularTurnAction action) {
        System.out.printf("\n--- Joueur ayant le rôle %s ---\n", player.getCurrentRole().toColorizedString());

        if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
            System.out.println("Prend deux pièces d'or");
        } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
            System.out.println("Pioche deux cartes");
            System.out.printf("    garde    %s,\n", action.getChosenCard().toColorizedString());
            System.out.printf("    défausse %s\n", action.getDiscardedCard().toColorizedString());
        }

        if (action.getBuiltDistrict() != null) {
            System.out.printf("Construit le quartier %s\n", action.getBuiltDistrict().toColorizedString());
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
