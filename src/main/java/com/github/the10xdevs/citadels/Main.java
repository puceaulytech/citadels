package com.github.the10xdevs.citadels;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(List.of(new DummyBehavior(), new DummyBehavior(), new DummyBehavior(), new DummyBehavior()));
        game.start();
    }
}
