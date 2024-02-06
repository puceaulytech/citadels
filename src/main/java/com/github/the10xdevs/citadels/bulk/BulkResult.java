package com.github.the10xdevs.citadels.bulk;

public class BulkResult {
    private int wins;
    private int loses;
    private double averageScore;

    public BulkResult() {
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

    public void setAverageScore(double amount) {
        this.averageScore = amount;
    }

    public void addWin() {
        this.wins++;
    }

    public void addWin(int amount) {
        this.wins += amount;
    }

    public void addLoss() {
        this.loses++;
    }

    public void addLoss(int amount) {
        this.loses += amount;
    }

    public void incrementAverageScore(double amount) {
        this.averageScore += amount;
    }

    public double getWinPercentage(int iterations) {
        return (this.getWins() * 100.0) / iterations;
    }
}
