package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;

/**
 * An immutable view of a Player
 * @see Player
 */
public class PlayerView {
    protected final Player player;

    public PlayerView(Player player) {
        this.player = player;
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
}
