package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;

public class ArchitecteAbilityAction extends AbilityAction {
    private int maxDistricts = 3;
    /**
     * A flag indicating whether the Architect has already drawn additional cards during the turn.
     */
    private boolean hasDrawnCards = false;

    /**
     * Constructs an ArchitecteAbilityAction instance for the given current player and game.
     *
     * @param currentPlayer The player performing the ability action.
     * @param game          The current game state.
     */

    public ArchitecteAbilityAction(Player currentPlayer, Game game) {
        super(currentPlayer, game);
    }

    /**
     * Draws additional cards for the Architect.
     *
     * @throws IllegalActionException If drawing additional cards is not allowed (can only be done once per turn).
     */

    public void drawAdditionalCards() throws IllegalActionException {
        if (hasDrawnCards)
            throw new IllegalActionException("Architect can only draw additional cards once");

        for (int i = 0; i <= 2; i++) {
            this.currentPlayer.getHand().add(this.game.getDeck().drawCard());
        }

        this.hasDrawnCards = true;
    }

    /**
     * Builds districts for the Architect in a single turn.
     *
     * @param districts The list of districts to build.
     * @throws IllegalActionException If the number of districts to build exceeds the maximum allowed.
     */


    public void buildDistricts(List<District> districts) throws IllegalActionException {
        if (districts.size() > this.maxDistricts) {
            throw new IllegalActionException("Architecte can only build up to three districts in one turn");
        }

        for (District district : districts) {
            this.currentPlayer.buildDistrict(district);
        }

        this.maxDistricts -= districts.size();
    }

    /**
     * Gets the remaining maximum number of districts the Architect can build in the current turn.
     *
     * @return The remaining maximum number of districts.
     */
    public int getRemainingMaxDistricts() {
        return this.maxDistricts;
    }
}