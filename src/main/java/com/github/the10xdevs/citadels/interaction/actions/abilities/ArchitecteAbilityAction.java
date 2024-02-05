package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;

public class ArchitecteAbilityAction extends AbilityAction {
    private int maxDistricts = 3;
    private boolean hasDrawnCards = false;

    public ArchitecteAbilityAction(Player currentPlayer, Game game) {
        super(currentPlayer, game);
    }

    public void drawAdditionalCards() throws IllegalActionException {
        if (hasDrawnCards)
            throw new IllegalActionException("Architect can only draw additional cards once");

        for (int i = 0; i <= 2; i++) {
            this.currentPlayer.getHand().add(this.game.getDeck().drawCard());
        }

        this.hasDrawnCards = true;
    }


    public void buildDistricts(List<District> districts) throws IllegalActionException {
        if (districts.size() > this.maxDistricts) {
            throw new IllegalActionException("Architecte can only build up to three districts in one turn");
        }

        for (District district : districts) {
            this.currentPlayer.buildDistrict(district);
        }

        this.maxDistricts -= districts.size();
    }


    public int getRemainingMaxDistricts() {
        return this.maxDistricts;
    }
}