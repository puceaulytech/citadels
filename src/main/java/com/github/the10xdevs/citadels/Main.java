package com.github.the10xdevs.citadels;

import com.beust.jcommander.JCommander;
import com.github.the10xdevs.citadels.bulk.BulkRunner;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.interaction.behaviors.*;

import java.util.List;

public class Main {
    public static void main(String[] args) {
        Arguments arguments = new Arguments();

        JCommander.newBuilder()
                .addObject(arguments)
                .build()
                .parse(args);

        if (arguments.twoThousand) {
            BulkRunner firstBulkRunner = new BulkRunner(1000, List.of(
                    new RichardBehavior(),
                    new RandomBehavior(),
                    new ExpensiveBuilderBehavior(),
                    new FastBuilderBehavior(),
                    new TryharderBehavior()
            ));

            firstBulkRunner.run();
            firstBulkRunner.printStats();

            System.out.println();

            BulkRunner secondBulkRunner = new BulkRunner(1000, List.of(
                    new TryharderBehavior(),
                    new TryharderBehavior(),
                    new TryharderBehavior(),
                    new TryharderBehavior()
            ));

            secondBulkRunner.run();
            secondBulkRunner.printStats();
        }

        if (arguments.csv) {
            BulkRunner bulkRunner = new BulkRunner(1000, List.of(
                    new RandomBehavior(),
                    new TryharderBehavior(),
                    new ExpensiveBuilderBehavior(),
                    new FastBuilderBehavior(),
                    new RichardBehavior()
            ));

            bulkRunner.run();
            try {
                bulkRunner.writeToCSV();
            } catch (Exception e) {
                System.out.println("Error encountered when writing to CSV file");
                e.printStackTrace();
            }
        }

        if (arguments.demo) {
            Game game = GameBuilder.create()
                    .addBehavior(new RandomBehavior())
                    .addBehavior(new TryharderBehavior())
                    .addBehavior(new ExpensiveBuilderBehavior())
                    .addBehavior(new FastBuilderBehavior())
                    .addBehavior(new RichardBehavior())
                    .build();

            game.start();
        }
    }
}