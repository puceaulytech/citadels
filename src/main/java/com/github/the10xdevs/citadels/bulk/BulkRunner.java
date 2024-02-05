package com.github.the10xdevs.citadels.bulk;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Leaderboard;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.logging.VoidLogger;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkRunner {
    private final Map<Behavior, Result> scores = new HashMap<>();
    private final int iterations;

    public BulkRunner(int iterations, List<Behavior> behaviors) {
        this.iterations = iterations;

        for (Behavior b : behaviors) {
            this.scores.put(b, new Result());
        }
    }

    public void run() {
        for (int i = 0; i < iterations; i++) {
            GameBuilder gameBuilder = GameBuilder.create()
                    .withLogger(new VoidLogger());

            for (Behavior behavior : this.scores.keySet()) {
                gameBuilder.addBehavior(behavior);
            }

            Game game = gameBuilder.build();

            Leaderboard leaderboard = game.start();

            for (int j = 0; j < leaderboard.getEntries().size(); j++) {
                Leaderboard.Entry entry = leaderboard.getEntries().get(j);
                Result bulkRunnerResult = this.scores.get(entry.getPlayer().getBehavior());

                if (j == 0) {
                    bulkRunnerResult.addWin();
                } else {
                    bulkRunnerResult.addLoss();
                }

                bulkRunnerResult.incrementAverageScore((double) entry.getScore() / iterations);
            }
        }
    }

    public void printStats() {
        for (Map.Entry<Behavior, Result> entry : this.scores.entrySet()) {
            Behavior behavior = entry.getKey();
            Result result = entry.getValue();

            double winPercentage = (result.getWins() * 100.0) / this.iterations;

            System.out.format("%s - W/L %d/%d %.2f%% - avg %.1f\n", behavior.getName(), result.getWins(), result.getLoses(), winPercentage, result.getAverageScore());
        }
    }


    public static class Result {
        private int wins;
        private int loses;
        private double averageScore;

        public Result() {
            this.wins = 0;
            this.loses = 0;
            this.averageScore = 0;
        }

        public int getWins() {
            return this.wins;
        }

        public int getLoses() {
            return this.loses;
        }

        public double getAverageScore() {
            return this.averageScore;
        }

        public void addWin() {
            this.wins++;
        }

        public void addLoss() {
            this.loses++;
        }

        public void incrementAverageScore(double amount) {
            this.averageScore += amount;
        }
    }
}
