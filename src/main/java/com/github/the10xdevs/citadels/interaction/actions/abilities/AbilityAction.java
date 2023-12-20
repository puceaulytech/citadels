package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;

/**
 * Actions performed concerning role abilities
 */
public abstract class AbilityAction {
    protected final Player currentPlayer;
    protected final Game game;

    protected AbilityAction(Player currentPlayer, Game game) {
        this.currentPlayer = currentPlayer;
        this.game = game;
    }

    protected AbilityAction(Game game) {
        this(null, game);
    }
}
