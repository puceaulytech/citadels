package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.Role;

/**
 * An immutable view of a Player
 *
 * @see Player
 */
public class PlayerView {
    protected final Player player;
    private final boolean roleKnownToAll;

    public PlayerView(Player player) {
        this(player, false);
    }

    public PlayerView(Player player, boolean roleKnownToAll) {
        this.player = player;
        this.roleKnownToAll = roleKnownToAll;
    }

    public int getGold() {
        return this.player.getGold();
    }

    public CityView getCity() {
        return new CityView(this.player.getCity());
    }

    public int getHandSize() {
        return this.player.getHand().size();
    }

    public boolean represents(Player targetPlayer) {
        return this.player.equals(targetPlayer);
    }

    public String getName() {
        return this.player.getName();
    }

    public Role getCurrentRole() {
        return this.roleKnownToAll ? this.player.getCurrentRole() : null;
    }
}
