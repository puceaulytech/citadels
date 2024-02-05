package com.github.the10xdevs.citadels;

import com.github.the10xdevs.citadels.bulk.BulkRunner;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.interaction.behaviors.ExpensiveBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        BulkRunner firstBulkRunner = new BulkRunner(1000, List.of(
                new ExpensiveBuilderBehavior(),
                new FastBuilderBehavior(),
                new RandomBehavior(),
                new RandomBehavior()
        ));

        firstBulkRunner.run();
        firstBulkRunner.printStats();
    }
}