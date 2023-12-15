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

    /**
     * Take gold on this turn
     * @throws IllegalActionException If the action is invalid
     */
    public void takeGold() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot take gold because an action has already been performed");
        this.basicAction = BasicAction.GOLD;
    }

    /**
     * Draw two cards on this turn
     * @return The two cards
     * @throws IllegalActionException If the action was invalid
     */
    public Pair<District, District> drawCards() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot draw cards because an action has already been performed");
        if (this.cardsToDraw.isEmpty())
            throw new IllegalActionException("Cannot draw cards because the deck is empty");
        this.basicAction = BasicAction.CARDS;
        return this.cardsToDraw;
    }

    /**
     * Choose a card that was drawn
     * @param district The card to choose
     * @throws IllegalActionException If the action was invalid
     */
    public void chooseCard(District district) throws IllegalActionException {
        if (this.basicAction != BasicAction.CARDS)
            throw new IllegalActionException("Cannot choose card because no cards were drawn");
        if (this.chosenCard != null)
            throw new IllegalActionException("Cannot choose card because a card was already chosen");
        if (district == null)
            throw new IllegalActionException("Chosen district is null");
        if (!this.cardsToDraw.contains(district))
            throw new IllegalActionException("Cannot choose a card that is not at the top of deck");
        this.chosenCard = district;
    }

    /**
     * Build a district on this turn
     * @param district The district to build
     * @throws IllegalActionException If the action was invalid
     */
    public void buildDistrict(District district) throws IllegalActionException {
        if (district == null)
            throw new IllegalActionException("Cannot build a district that is null");
        if (!(this.currentPlayerView.getHand().contains(district) || district.equals(this.chosenCard)))
            throw new IllegalActionException("Cannot build a district that is not in hand");
        if ((this.currentPlayerView.getGold() + (this.basicAction == BasicAction.GOLD ? 2 : 0)) < district.getCost())
            throw new IllegalActionException("Cannot build district without enough gold");
        if (this.builtDistrict != null)
            throw new IllegalActionException("Cannot build multiple districts in one turn");
        this.builtDistrict = district;
    }

    public boolean canDraw() {
        return !this.cardsToDraw.isEmpty();
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

    /**
     * An enum representing what was done during the turn
     */
    public enum BasicAction {
        GOLD,
        CARDS,
    }
}