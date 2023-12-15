package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Game;

import java.util.List;

/**
 * An immutable view of a Game
 * @see Game
 */
public final class GameView {
    private final Game game;

    public GameView(Game game) {
        this.game = game;
    }

    public List<PlayerView> getPlayers() {
        return this.game.getPlayers().stream().map(PlayerView::new).toList();
    }

    public int getDeckSize() {
        return this.game.getDeck().getCardsCount();
    }
}
