package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.ArchitecteAbilityAction;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;

import java.util.List;
import java.util.Optional;

/**
 * A class used by Behaviors to store the actions they want to perform
 *
 * @see com.github.the10xdevs.citadels.interaction.behaviors.Behavior
 */
public class RegularTurnAction {
    private final Player currentPlayer;
    private final Deck<District> deck;
    private final Pair<District, Optional<District>> cardsToDraw;
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
     */
    public RegularTurnAction(Game game, Player currentPlayer) {
        this.currentPlayer = currentPlayer;
        this.deck = game.getDeck();
        this.abilityAction = currentPlayer.getCurrentRole().getAbilityAction(currentPlayer, game);
        this.cardsToDraw = this.deck.peekFirstTwo();
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
    public Pair<District, Optional<District>> drawCards() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot draw cards because an action has already been performed");
        if (!this.canDraw())
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
        if (!this.cardsToDraw.first().equals(district) && !this.cardsToDraw.second().equals(Optional.of(district)))
            throw new IllegalActionException("Cannot choose a card that is not at the top of deck");
        this.chosenCard = district;

        // To arrive here there is necessarily at least one card in the deck,
        // so we can safely draw one card
        this.deck.drawCard();
        currentPlayer.getHand().add(this.getChosenCard());

        Optional<District> discardedCard = this.getDiscardedCard();
        if (discardedCard.isPresent()) {
            this.deck.drawCard();
            this.deck.enqueueCard(discardedCard.get());
        }
    }

    /**
     * Build a district on this turn
     *
     * @param district The district to build
     * @throws IllegalActionException If the action was invalid
     */
    public void buildDistrict(District district) throws IllegalActionException {
        if (this.builtDistrict != null)
            throw new IllegalActionException("Cannot build multiple districts in one turn");

        // If the current player has Architect Role, redirect handling of building to the architect ability action
        // This is done so that the player cannot use both normal building way and architect building way
        if (this.currentPlayer.getCurrentRole() == Role.ARCHITECTE) {
            ((ArchitecteAbilityAction) this.abilityAction).buildDistricts(List.of(district));
        } else {
            this.currentPlayer.buildDistrict(district);
            this.builtDistrict = district;
        }
    }

    /**
     * Returns true if there is at least one card in the deck
     *
     * @return true if there is at least one card in the deck
     */
    public boolean canDraw() {
        return this.cardsToDraw.first() != null;
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
    public Optional<District> getDiscardedCard() {
        return this.chosenCard.equals(this.cardsToDraw.first()) ? cardsToDraw.second() : Optional.of(cardsToDraw.first());
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