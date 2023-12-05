package com.github.the10xdevs.citadels.models;

import java.util.ArrayList;
import java.util.List;

/**
 * A district in the game
 */
public final class District {
    private final String name;
    private final Category category;
    private final int cost;

    public District(String name, Category category, int cost) {
        this.name = name;
        this.category = category;
        this.cost = cost;
    }

    /**
     * Get a list of all districts in the game
     * @return All the districts
     */
    public static List<District> all() {
        List<District> allDistricts = new ArrayList<>();
        addAmount(allDistricts, new District("Bibliothèque", Category.MERVEILLE, 6), 1);
        addAmount(allDistricts, new District("Caserne", Category.MILITAIRE, 3), 3);
        addAmount(allDistricts, new District("Cathédrale", Category.RELIGIEUX, 5), 2);
        addAmount(allDistricts, new District("Château", Category.NOBLE, 4), 4);
        addAmount(allDistricts, new District("Cimetière", Category.MERVEILLE, 5), 1);
        addAmount(allDistricts, new District("Comptoir", Category.MARCHAND, 3), 3);
        addAmount(allDistricts, new District("Cour des miracles", Category.MERVEILLE, 2), 1);
        addAmount(allDistricts, new District("Donjon", Category.MERVEILLE, 3), 2);
        addAmount(allDistricts, new District("Dracoport", Category.MERVEILLE, 8), 1);
        addAmount(allDistricts, new District("Echoppe", Category.MARCHAND, 2), 3);
        addAmount(allDistricts, new District("Ecole de magie", Category.MERVEILLE, 6), 1);
        addAmount(allDistricts, new District("Eglise", Category.RELIGIEUX, 2), 4);
        addAmount(allDistricts, new District("Forge", Category.MERVEILLE, 5), 1);
        addAmount(allDistricts, new District("Forteresse", Category.MILITAIRE, 5), 2);
        addAmount(allDistricts, new District("Hotel de ville", Category.MARCHAND, 5), 2);
        addAmount(allDistricts, new District("Laboratoire", Category.MERVEILLE, 5), 1);
        addAmount(allDistricts, new District("Manoir", Category.NOBLE, 3), 5);
        addAmount(allDistricts, new District("Marché", Category.MARCHAND, 2), 4);
        addAmount(allDistricts, new District("Monastère", Category.RELIGIEUX, 3), 3);
        addAmount(allDistricts, new District("Observatoire", Category.MERVEILLE, 5), 1);
        addAmount(allDistricts, new District("Palais", Category.NOBLE, 5), 2);
        addAmount(allDistricts, new District("Port", Category.MARCHAND, 4), 3);
        addAmount(allDistricts, new District("Prison", Category.MILITAIRE, 2), 3);
        addAmount(allDistricts, new District("Taverne", Category.MARCHAND, 1), 5);
        addAmount(allDistricts, new District("Temple", Category.RELIGIEUX, 1), 3);
        addAmount(allDistricts, new District("Tour de guet", Category.MILITAIRE, 1), 3);
        addAmount(allDistricts, new District("Université", Category.MERVEILLE, 6), 1);
        return allDistricts;
    }

    /**
     * Add a district multiple times into a list of districts
     * @param dest The list
     * @param district The district
     * @param amount The amount of time to add
     */
    private static void addAmount(List<District> dest, District district, int amount) {
        for (int i = 0; i < amount; i++) {
            dest.add(district);
        }
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

    /**
     * Get a colorized string of the district
     * @return The colorized string
     */
    public String toColorizedString() {
        return String.format("%s, %s, prix: %d", this.name, this.category.colorizeText(this.category.toString()), this.cost);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof District d)
            return this.name.equals(d.name) && this.cost == d.cost && this.category.equals(d.category);
        return false;
    }
}
