package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Set;

/**
 * Playing interface
 * <br>
 * Classes that want to 'play' must implement this interface
 */
public interface Behavior {
    /**
     * Pick a role from available roles
     *
     * @param action         The action
     * @param availableRoles Available roles
     */
    void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException;


    /**
     * Play a regular turn
     * <br>
     * This method takes an immutable view of a player and a game, because we don't trust the player and don't want it to
     * access internal data of the game state (like the deck, other player hands and gold, etc...), or mutate the state.
     *
     * @param action    The action
     * @param self      A view of the current player
     * @param gameState A view of the entire game state
     */
    void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException;
}
