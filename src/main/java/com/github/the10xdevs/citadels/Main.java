package com.github.the10xdevs.citadels;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.interaction.behaviors.ExpensiveBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.TryharderBehavior;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Game game = new Game(List.of(new RandomBehavior(), new TryharderBehavior(), new FastBuilderBehavior(), new ExpensiveBuilderBehavior()));
        game.start();
    }
}