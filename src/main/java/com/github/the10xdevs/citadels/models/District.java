package com.github.the10xdevs.citadels.models;

import java.util.Set;

public final class District {
    /**
     * List of all the districts in the game
     */
    public static final Set<District> all = Set.of(
            new District("Bibliothèque", Category.MERVEILLE, 6), // x1
            new District("Caserne", Category.MILITAIRE, 3), // x3
            new District("Cathédrale", Category.RELIGIEUX, 5), // x2
            new District("Château", Category.NOBLE, 4), // x4
            new District("Cimetière", Category.MERVEILLE, 5), // x1
            new District("Comptoir", Category.MARCHAND, 3), // x3
            new District("Cour des miracles", Category.MERVEILLE, 2), // x1
            new District("Donjon", Category.MERVEILLE, 3), // x2
            new District("Dracoport", Category.MERVEILLE, 8), // x1
            new District("Echoppe", Category.MARCHAND, 2), // x3
            new District("Ecole de magie", Category.MERVEILLE, 6), // x1
            new District("Eglise", Category.RELIGIEUX, 2), // x4
            new District("Forge", Category.MERVEILLE, 5), // x1
            new District("Forteresse", Category.MILITAIRE, 5), // x2
            new District("Hotel de ville", Category.MARCHAND, 5), // x2
            new District("Laboratoire", Category.MERVEILLE, 5), // x1
            new District("Manoir", Category.NOBLE, 3), // x5
            new District("Marché", Category.MARCHAND, 2), // x4
            new District("Monastère", Category.RELIGIEUX, 3), // x3
            new District("Observatoire", Category.MERVEILLE, 5), // x1
            new District("Palais", Category.NOBLE, 5), // x2
            new District("Port", Category.MARCHAND, 4), // x3
            new District("Prison", Category.MILITAIRE, 2), // x3
            new District("Taverne", Category.MARCHAND, 1), // x5
            new District("Temple", Category.RELIGIEUX, 1), // x3
            new District("Tour de guet", Category.MILITAIRE, 1), // x3
            new District("Université", Category.MERVEILLE, 6) // x1
    );
    private final String name;
    private final Category category;
    private final int cost;

    public District(String name, Category category, int cost) {
        this.name = name;
        this.category = category;
        this.cost = cost;
    }

    public Category getCategory() {
        return this.category;
    }

    public int getCost() {
        return this.cost;
    }

    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, prix: %d", this.name, this.category, this.cost);
    }

    public String toColorizedString() {
        return String.format("%s, %s, prix: %d", this.name, this.category.colorizeText(this.category.toString()), this.cost);
    }
}
