package com.github.the10xdevs.citadels.bulk;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Leaderboard;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.logging.VoidLogger;
import dnl.utils.text.table.TextTable;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BulkRunner {
    private final Map<Behavior, BulkResult> scores = new HashMap<>();
    private final int iterations;
    private final String[] headers = {"Behavior", "Wins", "Loses", "%", "Average score"};

    public BulkRunner(int iterations, List<Behavior> behaviors) {
        this.iterations = iterations;

        for (Behavior b : behaviors) {
            this.scores.put(b, new BulkResult());
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
                BulkResult bulkRunnerResult = this.scores.get(entry.getPlayer().getBehavior());

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
        Object[][] data = getRawStats();

        TextTable tt = new TextTable(headers, data);
        tt.printTable();
    }

    private Object[][] getRawStats() {
        Object[][] data = new Object[this.scores.size()][this.headers.length];

        int i = 0;
        for (Map.Entry<Behavior, BulkResult> entry : this.scores.entrySet()) {
            Behavior behavior = entry.getKey();
            BulkResult bulkResult = entry.getValue();

            data[i][0] = behavior.getName();
            data[i][1] = bulkResult.getWins();
            data[i][2] = bulkResult.getLoses();
            data[i][3] = bulkResult.getWinPercentage(this.iterations);
            data[i][4] = String.format("%.2f", bulkResult.getAverageScore());
            i++;
        }
        return data;
    }
}
