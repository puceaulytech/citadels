package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;
import java.util.Optional;

public class MagicienAbilityAction extends AbilityAction {
    private PlayerView exchangedPlayer;

    /**
     * Constructor for MagicienAbilityAction.
     *
     * @param currentPlayer the current player
     * @param game          the current game state
     */

    public MagicienAbilityAction(Player currentPlayer, Game game) {
        super(currentPlayer, game);
    }

    /**
     * Exchanges the hand of the current player with the hand of the target player.
     *
     * @param targetPlayer the player to exchange hands with
     * @throws IllegalActionException if the provided player view does not refer to an existing player
     */

    public void exchangeHandWith(PlayerView targetPlayer) throws IllegalActionException {
        this.exchangedPlayer = targetPlayer;

        Optional<Player> player = this.findPlayerByView(targetPlayer);

        if (player.isEmpty())
            throw new IllegalActionException("Provided player view does not refer to an existing player");

        this.currentPlayer.swapHandWith(player.get());
    }

    /**
     * Discards the specified cards from the current player's hand and draws the same number of cards from the deck.
     *
     * @param cards the cards to be discarded
     * @throws IllegalActionException if some provided cards are not in the player's hand
     */

    public void discardAndDraw(List<District> cards) throws IllegalActionException {
        if (cards.stream().anyMatch(card -> !this.currentPlayer.getHand().contains(card)))
            throw new IllegalActionException("Some provided cards are not in the player's hand");

        for (District district : cards) {
            this.game.getDeck().enqueueCard(district);
        }

        for (int i = 0; i < cards.size(); i++) {
            this.currentPlayer.getHand().add(this.game.getDeck().drawCard());
        }
    }

    /**
     * Returns the player with whom the current player exchanged hands.
     *
     * @return the player with whom the current player exchanged hands
     */

    public PlayerView getExchangedPlayer() {
        return this.exchangedPlayer;
    }
}
