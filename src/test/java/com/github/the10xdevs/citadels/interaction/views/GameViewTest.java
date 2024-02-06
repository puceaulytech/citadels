package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameViewTest {
    @Test
    void cannotAddPlayer() {
        GameView gameView = new GameView(GameBuilder.create().build());
        Player player = new Player(new FastBuilderBehavior());
        assertThrows(Exception.class, () -> gameView.getPlayers().add(new PlayerView(player)));
    }

    @Test
    void getDeckSizeException() {
        Game game = GameBuilder.create().addBehavior(new FastBuilderBehavior()).build();
        GameView gameView = new GameView(game);
        assertEquals(65, gameView.getDeckSize());
    }

    @Test
    void getRolesFacingUpException() {
        GameView gameView = new GameView(GameBuilder.create().build());
        assertEquals(0, gameView.getRolesFacingUp().size());
    }
}