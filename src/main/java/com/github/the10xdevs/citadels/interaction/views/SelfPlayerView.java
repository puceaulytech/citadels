package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Collections;
import java.util.List;

/**
 * An immutable view of a Player, with extra information that can only be accessed by the targeted player
 * @see Player
 */
public final class SelfPlayerView extends PlayerView {
    public SelfPlayerView(Player player) {
        super(player);
    }

    public List<District> getHand() {
        return Collections.unmodifiableList(this.player.getHand());
    }

    public Role getCurrentRole() {
        if (this.player.getCurrentRole() == null) {
            // Handle the case where the role is null, either throw an exception or provide a default role.
            throw new IllegalStateException("Current role is null for the player.");
        }
        return this.player.getCurrentRole();
    }

}
