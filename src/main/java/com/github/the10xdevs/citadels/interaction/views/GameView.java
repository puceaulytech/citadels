package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.models.Role;

import java.util.List;
import java.util.Optional;

/**
 * An immutable view of a Game
 *
 * @see Game
 */
public final class GameView {
    private final Game game;

    public GameView(Game game) {
        this.game = game;
    }

    public List<PlayerView> getPlayers() {
        return this.game.getPlayers()
                .stream()
                .map(player -> new PlayerView(
                        player,
                        this.game.getCurrentTurnOrder() >= player.getCurrentRole().getTurnOrder()
                ))
                .toList();
    }

    public int getDeckSize() {
        return this.game.getDeck().getCardsCount();
    }

    public Optional<Role> getKilledRole() {
        return this.game.getKilledRole();
    }
}
