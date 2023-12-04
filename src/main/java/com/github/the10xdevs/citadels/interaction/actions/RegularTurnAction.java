package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;

public class RegularTurnAction {
    private final AbilityAction abilityAction;
    private final Pair<District, District> cardsToDraw;

    private BasicAction basicAction;
    private District chosenCard;

    public RegularTurnAction(AbilityAction abilityAction, Pair<District, District> cards) {
        this.abilityAction = abilityAction;
        this.cardsToDraw = cards;
    }

    public void takeGold() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot take gold because an action has already been performed");
        this.basicAction = BasicAction.GOLD;
    }

    public Pair<District, District> drawCards() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot draw cards because an action has already been performed");
        this.basicAction = BasicAction.CARDS;
        return this.cardsToDraw;
    }

    public void chooseCard(District district) throws IllegalActionException {
        if (this.basicAction != BasicAction.CARDS)
            throw new IllegalActionException("Cannot choose card because no cards were drawn");
        if (this.chosenCard != null)
            throw new IllegalActionException("Cannot choose card because a card was already chosen");
        if (!this.cardsToDraw.contains(district))
            throw new IllegalActionException("Cannot choose a card that is not at the top of deck");
        this.chosenCard = district;
    }

    public void buildDistrict(District district) {
        // TODO: do something
    }

    public AbilityAction getAbilityAction() {
        return this.abilityAction;
    }

    public BasicAction getBasicAction() {
        return this.basicAction;
    }

    public District getChosenCard() {
        return this.chosenCard;
    }

    public District getDiscardedCard() {
        return this.chosenCard.equals(this.cardsToDraw.first()) ? cardsToDraw.second() : cardsToDraw.first();
    }

    public enum BasicAction {
        GOLD,
        CARDS,
    }
}
