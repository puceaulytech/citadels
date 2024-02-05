package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class SelfPlayerViewTest {
    @SuppressWarnings("DataFlowIssue")
    @Test
    void cannotAddCard() {
        SelfPlayerView playerView = new SelfPlayerView(new Player(new FastBuilderBehavior()));
        assertThrows(Exception.class, () -> playerView.getHand().add(new District("de", Category.NOBLE, 10)));
    }

    @Test
    void getCurrentRole() {
        Player player = new Player(new FastBuilderBehavior());
        player.setCurrentRole(Role.MAGICIEN);

        SelfPlayerView playerView = new SelfPlayerView(player);

        assertEquals(Role.MAGICIEN, playerView.getCurrentRole());
    }
}