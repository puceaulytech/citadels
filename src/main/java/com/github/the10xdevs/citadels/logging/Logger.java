package com.github.the10xdevs.citadels.logging;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;

import java.util.List;

public interface Logger {
    /**
     * Log the start of a turn
     *
     * @param turn The turn number
     */
    void logTurnStart(int turn);

    /**
     * Log a role choosing action
     *
     * @param index  The index of the player
     * @param player The player
     * @param action The action made by the player
     */
    void logRoleTurnAction(int index, Player player, RoleTurnAction action);

    /**
     * Log a regular turn action
     *
     * @param player The player that made the action
     * @param action The action made by the player
     */
    void logRegularTurnAction(Player player, RegularTurnAction action);

    /**
     * Log winners of the game
     *
     * @param players All the players
     */
    void logWinners(List<Player> players, Player firstPlayerToFinish);

    /**
     * Log if an error occurs during the game
     *
     * @param error Caught error to be logged
     */
    void logError(Throwable error);
}
