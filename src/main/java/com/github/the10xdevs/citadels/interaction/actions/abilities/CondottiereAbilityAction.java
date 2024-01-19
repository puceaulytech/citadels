package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Optional;

public class CondottiereAbilityAction extends AbilityAction {
    public CondottiereAbilityAction(Player currentPlayer, Game game) {
        super(currentPlayer, game);
    }

    private PlayerView targetPlayer;
    private District targetDistrict;

    public void destroy(PlayerView targetPlayer, District targetDistrict) throws IllegalActionException {
        this.targetPlayer = targetPlayer;
        this.targetDistrict = targetDistrict;

        Optional<Player> player = this.findPlayerByView(targetPlayer);

        if (player.isEmpty())
            throw new IllegalActionException("Provided player view does not refer to an existing player");
        if (player.get().getCurrentRole() == Role.EVEQUE)
            throw new IllegalActionException("Cannot destroy district own by the Eveque");

        int destroyCost = targetDistrict.getCost() - 1;

        if (this.currentPlayer.getGold() < destroyCost)
            throw new IllegalActionException(String.format("Cannot destroy district: need %d but have %d", destroyCost, this.currentPlayer.getGold()));
        if (player.get().getCity().getSize() == 8)
            throw new IllegalActionException("Cannot destroy district: target player has 8 districts");
        if (!player.get().getCity().getDistricts().remove(targetDistrict))
            throw new IllegalActionException("Cannot destroy district: district not in target player's city");

        this.currentPlayer.incrementGold(-destroyCost);
    }

    public PlayerView getTargetPlayer() {
        return targetPlayer;
    }

    public District getTargetDistrict() {
        return targetDistrict;
    }
}
