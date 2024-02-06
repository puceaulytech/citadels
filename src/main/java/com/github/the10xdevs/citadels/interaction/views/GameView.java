package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Collections;
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

    /**
     * Constructor for GameView.
     *
     * @param game the game object to create a view of
     */

    public GameView(Game game) {
        this.game = game;
    }

    /**
     * Returns a list of PlayerView objects representing the players in the game.
     * If a player's current role is null, a PlayerView is created with just the player.
     * Otherwise, a PlayerView is created with the player and a boolean indicating if the current turn order is greater than or equal to the player's current role's turn order.
     *
     * @return a list of PlayerView objects
     */
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

    /**
     * Returns the size of the game's deck.
     *
     * @return the size of the deck
     */

    public int getDeckSize() {
        return this.game.getDeck().getCardsCount();
    }

    /**
     * Returns an unmodifiable set of roles that are facing up in the game.
     *
     * @return a set of roles facing up
     */
    public Set<Role> getRolesFacingUp() {
        return Collections.unmodifiableSet(this.game.getRolesFacingUp());
    }

    /**
     * Returns an Optional containing the role that was killed in the game, if any.
     *
     * @return an Optional containing the killed role, or an empty Optional if no role was killed
     */
    public Optional<Role> getKilledRole() {
        return this.game.getKilledRole();
    }

    public int getTurn() {
        return this.game.getTurn();
    }
}
