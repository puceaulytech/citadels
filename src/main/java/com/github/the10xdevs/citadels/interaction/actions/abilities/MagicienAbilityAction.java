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

    public MagicienAbilityAction(Player currentPlayer, Game game) {
        super(currentPlayer, game);
    }

    public void exchangeHandWith(PlayerView targetPlayer) throws IllegalActionException {
        this.exchangedPlayer = targetPlayer;

        Optional<Player> player = this.game.getPlayers()
                .stream()
                .filter(targetPlayer::represents)
                .findFirst();

        if (player.isEmpty())
            throw new IllegalActionException("Provided player view does not refer that an existing player");

        this.currentPlayer.swapHandWith(player.get());
    }

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

    public PlayerView getExchangedPlayer() {
        return this.exchangedPlayer;
    }
}
