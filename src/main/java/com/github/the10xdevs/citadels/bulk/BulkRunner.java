package com.github.the10xdevs.citadels.bulk;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Leaderboard;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.logging.VoidLogger;
import com.opencsv.CSVReader;
import com.opencsv.CSVWriter;
import com.opencsv.exceptions.CsvException;
import dnl.utils.text.table.TextTable;

import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class BulkRunner {
    private final Map<Behavior, BulkResult> scores = new HashMap<>();
    private final List<Behavior> behaviors;
    private final String[] headers = {"Behavior", "Wins", "Loses", "%", "Average score"};
    private int iterations;

    public BulkRunner(int iterations, List<Behavior> behaviors) {
        this.iterations = iterations;
        this.behaviors = behaviors;

        for (Behavior b : behaviors) {
            this.scores.put(b, new BulkResult());
        }
    }

    public void run() {
        for (int i = 0; i < iterations; i++) {
            GameBuilder gameBuilder = GameBuilder.create()
                    .withLogger(new VoidLogger());

            for (Behavior behavior : this.behaviors) {
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
        String[][] data = this.getRawStats();

        TextTable tt = new TextTable(headers, data);
        tt.printTable();
    }

    private String[][] getRawStats() {
        String[][] data = new String[this.scores.size()][this.headers.length];

        List<Map.Entry<Behavior, BulkResult>> sortedEntries = this.scores.entrySet()
                .stream()
                .sorted(Comparator.comparingInt((Map.Entry<Behavior, BulkResult> e) -> e.getValue().getWins()).reversed())
                .toList();

        int i = 0;
        for (Map.Entry<Behavior, BulkResult> entry : sortedEntries) {
            Behavior behavior = entry.getKey();
            BulkResult bulkResult = entry.getValue();

            data[i][0] = behavior.getName();
            data[i][1] = String.valueOf(bulkResult.getWins());
            data[i][2] = String.valueOf(bulkResult.getLoses());
            data[i][3] = String.valueOf(bulkResult.getWinPercentage(this.iterations));
            data[i][4] = String.valueOf(bulkResult.getAverageScore());
            i++;
        }
        return data;
    }

    private void mergeExistingCSV(File csvFile) throws IOException, CsvException {
        try (CSVReader reader = new CSVReader(new FileReader(csvFile))) {
            List<String[]> allRows = reader.readAll();
            List<String[]> dataRows = allRows.subList(1, allRows.size());

            int previousGameCount = Integer.parseInt(dataRows.get(0)[1]) + Integer.parseInt(dataRows.get(0)[2]);
            this.iterations += previousGameCount;

            for (String[] dataRow : dataRows) {
                String behaviorName = dataRow[0];

                for (Map.Entry<Behavior, BulkResult> entry : this.scores.entrySet()) {
                    if (entry.getKey().getName().equals(behaviorName)) {
                        BulkResult bulkResult = entry.getValue();

                        bulkResult.addWin(Integer.parseInt(dataRow[1]));
                        bulkResult.addLoss(Integer.parseInt(dataRow[2]));
                        bulkResult.setAverageScore((bulkResult.getAverageScore() + Double.parseDouble(dataRow[4])) / 2);

                        break;
                    }
                }
            }
        }
    }

    public void writeToCSV() throws CsvException, IOException {
        Path filePath = Paths.get("stats", "gamestats.csv");
        File csvFile = filePath.toFile();

        if (csvFile.exists() && !csvFile.isDirectory()) {
            this.mergeExistingCSV(csvFile);
        } else {
            File statDir = Paths.get("stats").toFile();
            if (!statDir.exists() && !statDir.mkdir()) {
                return;
            }
        }

        CSVWriter writer = new CSVWriter(new FileWriter(csvFile));
        String[][] data = this.getRawStats();

        writer.writeNext(this.headers, false);
        writer.writeAll(Arrays.stream(data).toList(), false);

        writer.close();
    }
}
