package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Collections;
import java.util.List;

/**
 * An immutable view of a Player, with extra information that can only be accessed by the targeted player
 *
 * @see Player
 */
public final class SelfPlayerView extends PlayerView {
    public SelfPlayerView(Player player) {
        super(player);
    }

    public List<District> getHand() {
        return Collections.unmodifiableList(this.player.getHand());
    }

    @Override
    public Role getCurrentRole() {
        return this.player.getCurrentRole();
    }
}