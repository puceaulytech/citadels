package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.District;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class ArchitecteAbilityAction extends AbilityAction {
    private final List<District> builtDistricts = new ArrayList<>();
    private final List<District> drawnCards = new ArrayList<>();
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

        for (int i = 0; i < 2 && !this.game.getDeck().isEmpty(); i++) {
            District card = this.game.getDeck().drawCard();
            this.currentPlayer.getHand().add(card);
            this.drawnCards.add(card);
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
            this.builtDistricts.add(district);
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

    public List<District> getBuiltDistricts() {
        return Collections.unmodifiableList(this.builtDistricts);
    }

    public List<District> getDrawnCards() {
        return Collections.unmodifiableList(this.drawnCards);
    }
}