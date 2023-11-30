package com.github.the10xdevs.citadels.models;

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
}
