package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.logging.ConsoleLogger;

/**
 * A category in the game
 */
public enum Category {
    NOBLE,
    RELIGIEUX,
    MARCHAND,
    MILITAIRE,
    MERVEILLE;

    @Override
    public String toString() {
        return switch (this) {
            case NOBLE -> "Noble";
            case RELIGIEUX -> "Religieux";
            case MARCHAND -> "Marchand";
            case MILITAIRE -> "Militaire";
            case MERVEILLE -> "Merveille";
        };
    }

    /**
     * Get the corresponding ANSI color code
     * @return The color code
     */
    public String getANSIColorCode() {
        return switch (this) {
            case NOBLE -> ConsoleLogger.ANSI_YELLOW;
            case RELIGIEUX -> ConsoleLogger.ANSI_BLUE;
            case MARCHAND -> ConsoleLogger.ANSI_GREEN;
            case MILITAIRE -> ConsoleLogger.ANSI_RED;
            case MERVEILLE -> ConsoleLogger.ANSI_PURPLE;
        };
    }
}