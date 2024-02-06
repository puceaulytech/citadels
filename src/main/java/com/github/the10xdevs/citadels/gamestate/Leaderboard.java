package com.github.the10xdevs.citadels.gamestate;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class Leaderboard {
    public static class Entry {
        private final Player player;
        private final int score;

        public Entry(Player player, int score) {
            this.player = player;
            this.score = score;
        }

        public Player getPlayer() {
            return this.player;
        }

        public int getScore() {
            return this.score;
        }
    }

    private final List<Entry> entries = new ArrayList<>();

    public Leaderboard(List<Player> players, Player firstPlayerToFinish) {
        for (Player player : players) {
            this.entries.add(new Entry(player, player.getScore(player == firstPlayerToFinish)));
        }

        this.entries.sort(Comparator.comparingInt(Entry::getScore).reversed());
    }

    public List<Entry> getEntries() {
        return this.entries;
    }
}
