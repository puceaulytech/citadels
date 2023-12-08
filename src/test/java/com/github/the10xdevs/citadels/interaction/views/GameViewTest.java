package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class GameViewTest {
    @Test
    void cannotAddPlayer() {
        GameView gameView = new GameView(new Game(List.of()));
        Player player = new Player(new DummyBehavior());
        assertThrows(Exception.class, () -> gameView.getPlayers().add(new PlayerView(player)));
    }
}