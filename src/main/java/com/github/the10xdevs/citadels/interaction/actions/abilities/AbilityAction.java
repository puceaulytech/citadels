package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;

import java.util.Optional;

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

    protected Optional<Player> findPlayerByView(PlayerView targetPlayerView) {
        if (this.game == null) throw new IllegalStateException("Cannot find player when game is null");

        return this.game.getPlayers()
                .stream()
                .filter(targetPlayerView::represents)
                .findFirst();
    }
}
