package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;

/**
 * A class used by Behaviors to store the actions they want to perform
 *
 * @see com.github.the10xdevs.citadels.interaction.behaviors.Behavior
 */
public class RegularTurnAction {
    private final Player currentPlayer;
    private final Deck deck;
    private final Pair<District, District> cardsToDraw;
    private final AbilityAction abilityAction;

    private BasicAction basicAction;
    private District chosenCard;
    private District builtDistrict;

    /**
     * Constructs a RegularTurnAction with the player wanting to play
     * and the first two cards of the deck
     *
     * @param game          The current game
     * @param currentPlayer The current player
     * @param deck          The deck of the game
     */
    public RegularTurnAction(Game game, Player currentPlayer, Deck deck) {
        this.currentPlayer = currentPlayer;
        this.deck = deck;
        this.abilityAction = currentPlayer.getCurrentRole().getAbilityAction(currentPlayer, game);
        this.cardsToDraw = deck.peekFirstTwo();
    }

    /**
     * Take gold on this turn
     *
     * @throws IllegalActionException If the action is invalid
     */
    public void takeGold() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot take gold because an action has already been performed");
        this.basicAction = BasicAction.GOLD;

        currentPlayer.incrementGold(2);
    }

    /**
     * Draw two cards on this turn
     *
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
     *
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

        // To arrive here there is necessarily at least one card in the deck,
        // so we can safely draw one card
        this.deck.drawCard();
        currentPlayer.getHand().add(this.getChosenCard());
        if (!deck.isEmpty()) {
            this.deck.drawCard();
            this.deck.enqueueCard(this.getDiscardedCard());
        }
    }

    /**
     * Build a district on this turn
     *
     * @param district The district to build
     * @throws IllegalActionException If the action was invalid
     */
    public void buildDistrict(District district) throws IllegalActionException {
        if (district == null)
            throw new IllegalActionException("Cannot build a district that is null");
        if (!currentPlayer.getHand().contains(district))
            throw new IllegalActionException("Cannot build a district that is not in hand");
        if (currentPlayer.getGold() < district.getCost())
            throw new IllegalActionException("Cannot build district without enough gold");
        if (this.builtDistrict != null)
            throw new IllegalActionException("Cannot build multiple districts in one turn");
        this.builtDistrict = district;

        currentPlayer.incrementGold(-builtDistrict.getCost());
        currentPlayer.getHand().remove(builtDistrict);
        try {
            currentPlayer.getCity().addDistrict(builtDistrict);
        } catch (DuplicatedDistrictException e) {
            throw new IllegalActionException("Cannot build the same district twice", e);
        }
    }

    /**
     * Returns true if there is at least one card in the deck
     *
     * @return true if there is at least one card in the deck
     */
    public boolean canDraw() {
        return !this.cardsToDraw.isEmpty();
    }

    /**
     * Returns the Ability Action of the current player
     *
     * @return The Ability Action of the current player
     * @see AbilityAction
     */
    public AbilityAction getAbilityAction() {
        return this.abilityAction;
    }

    /**
     * Returns the basic action done by the player (either drawing or picking gold)
     *
     * @return The basic action done by the player
     */
    public BasicAction getBasicAction() {
        return this.basicAction;
    }

    /**
     * Returns the card chosen by the player between the two cards of the deck
     *
     * @return The card chosen by the player between the two cards of the deck
     */
    public District getChosenCard() {
        return this.chosenCard;
    }

    /**
     * Returns the card discarded by the player between the two cards of the deck
     *
     * @return The card discarded by the player between the two cards of the deck
     */
    public District getDiscardedCard() {
        return this.chosenCard.equals(this.cardsToDraw.first()) ? cardsToDraw.second() : cardsToDraw.first();
    }

    /**
     * Returns the district built by the current player
     *
     * @return The district built by the current player
     */
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