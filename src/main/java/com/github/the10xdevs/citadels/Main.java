package com.github.the10xdevs.citadels;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(List.of(new FastBuilderBehavior(), new FastBuilderBehavior(), new RandomBehavior(), new RandomBehavior()));
        game.start();
    }
}
