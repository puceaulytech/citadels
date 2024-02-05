package com.github.the10xdevs.citadels.logging;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;

import java.util.List;

public class VoidLogger implements Logger {
    @Override
    public void logTurnStart(int turn) {
        // Do nothing
    }

    @Override
    public void logRoleTurnAction(int index, Player player, RoleTurnAction action) {
        // Do nothing
    }

    @Override
    public void logRegularTurnAction(Player player, RegularTurnAction action) {
        // Do nothing
    }

    @Override
    public void logWinners(List<Player> players, Player firstPlayerToFinish) {
        // Do nothing
    }

    @Override
    public void logError(Throwable error) {
        // Do nothing??
    }
}