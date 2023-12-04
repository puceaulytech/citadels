package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;

public class RegularTurnAction {
    private final SelfPlayerView currentPlayerView;
    private final Pair<District, District> cardsToDraw;
    private final AbilityAction abilityAction;

    private BasicAction basicAction;
    private District chosenCard;
    private District builtDistrict;

    public RegularTurnAction(SelfPlayerView playerView, Pair<District, District> cards) {
        this.currentPlayerView = playerView;
        this.abilityAction = playerView.getCurrentRole().getAbilityAction();
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

    public void buildDistrict(District district) throws IllegalActionException {
        if (!(this.currentPlayerView.getHand().contains(district) || district.equals(this.chosenCard)))
            throw new IllegalActionException("Cannot build a district that is not in hand");
        if (this.currentPlayerView.getGold() < district.getCost())
            throw new IllegalActionException("Cannot build district without enough gold");
        if (this.builtDistrict != null)
            throw new IllegalActionException("Cannot build multiple districts in one turn");
        this.builtDistrict = district;
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

    public District getBuiltDistrict() {
        return this.builtDistrict;
    }

    public enum BasicAction {
        GOLD,
        CARDS,
    }
}