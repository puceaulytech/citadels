package com.github.the10xdevs.citadels;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.interaction.behaviors.ExpensiveBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;

public class Main {
    public static void main(String[] args) {
        Game game = GameBuilder.create()
                .addBehavior(new RandomBehavior())
                .addBehavior(new RandomBehavior())
                .addBehavior(new FastBuilderBehavior())
                .addBehavior(new ExpensiveBuilderBehavior())
                .build();

        game.start();
    }
}