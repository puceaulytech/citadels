package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.models.Role;

import java.util.List;
import java.util.Optional;
import java.util.Set;

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
                .map(player -> {
                    if (player.getCurrentRole() == null)
                        return new PlayerView(player);

                    return new PlayerView(
                            player,
                            this.game.getCurrentTurnOrder() >= player.getCurrentRole().getTurnOrder()
                    );
                })
                .toList();
    }

    public int getDeckSize() {
        return this.game.getDeck().getCardsCount();
    }

    public Set<Role> getRolesFacingUp() {
        return this.game.getRolesFacingUp();
    }

    public Optional<Role> getKilledRole() {
        return this.game.getKilledRole();
    }
}
