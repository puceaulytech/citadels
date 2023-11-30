package com.github.the10xdevs.citadels.models;

import java.util.Set;

public final class District {
    private final String name;
    private final Category category;
    private final int cost;

    public District(String name, Category category, int cost) {
        this.name = name;
        this.category = category;
        this.cost = cost;
    }

    public Category getCategory() { return this.category; }

    public int getCost() { return this.cost; }

    public String getName() { return this.name; }

    /**
     * List of all the districts in the game
     */
    public static final Set<District> all = Set.of(
        new District("Baraque de Logan", Category.MERVEILLE, 5)
    );

    @Override
    public String toString() {
        return String.format("%s, %s, prix: %d", this.name, this.category, this.cost);
    }
}
