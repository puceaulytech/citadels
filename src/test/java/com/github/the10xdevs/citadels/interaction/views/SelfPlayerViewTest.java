package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SelfPlayerViewTest {
    @SuppressWarnings("DataFlowIssue")
    @Test
    void cannotAddCard() {
        SelfPlayerView playerView =  new SelfPlayerView(new Player(new FastBuilderBehavior()));
        assertThrows(Exception.class, () -> playerView.getHand().add(new District("de", Category.NOBLE, 10)));
    }
}