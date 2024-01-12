package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.utils.ArrayUtils;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * A district in the game
 */
public final class District {
    private final String name;
    private final Category category;
    private final int cost;
    private final int score;

    /**
     * Constructs a District with a name, a {@link Category category}, a cost and a score
     *
     * @param name     The name of the district
     * @param category The category of the district
     * @param cost     The cost of the district
     * @param score    The score of the district
     */
    public District(String name, Category category, int cost, int score) {
        this.name = name;
        this.category = category;
        this.cost = cost;
        this.score = score;
    }

    /**
     * Create a new district, where its score is equal to its cost
     *
     * @param name     The name of the district
     * @param category The category of the district
     * @param cost     The cost of the district
     */
    public District(String name, Category category, int cost) {
        this(name, category, cost, cost);
    }


    /**
     * Get a list of all districts in the game
     *
     * @return All the districts
     */
    public static List<District> all() {
        List<District> allDistricts = new ArrayList<>();
        ArrayUtils.addAmount(allDistricts, new District("Bibliothèque", Category.MERVEILLE, 6), 1);
        ArrayUtils.addAmount(allDistricts, new District("Caserne", Category.MILITAIRE, 3), 3);
        ArrayUtils.addAmount(allDistricts, new District("Cathédrale", Category.RELIGIEUX, 5), 2);
        ArrayUtils.addAmount(allDistricts, new District("Château", Category.NOBLE, 4), 4);
        ArrayUtils.addAmount(allDistricts, new District("Cimetière", Category.MERVEILLE, 5), 1);
        ArrayUtils.addAmount(allDistricts, new District("Comptoir", Category.MARCHAND, 3), 3);
        ArrayUtils.addAmount(allDistricts, new District("Cour des miracles", Category.MERVEILLE, 2), 1);
        ArrayUtils.addAmount(allDistricts, new District("Donjon", Category.MERVEILLE, 3), 2);
        ArrayUtils.addAmount(allDistricts, new District("Dracoport", Category.MERVEILLE, 6, 8), 1);
        ArrayUtils.addAmount(allDistricts, new District("Echoppe", Category.MARCHAND, 2), 3);
        ArrayUtils.addAmount(allDistricts, new District("Ecole de magie", Category.MERVEILLE, 6), 1);
        ArrayUtils.addAmount(allDistricts, new District("Eglise", Category.RELIGIEUX, 2), 4);
        ArrayUtils.addAmount(allDistricts, new District("Forge", Category.MERVEILLE, 5), 1);
        ArrayUtils.addAmount(allDistricts, new District("Forteresse", Category.MILITAIRE, 5), 2);
        ArrayUtils.addAmount(allDistricts, new District("Hotel de ville", Category.MARCHAND, 5), 2);
        ArrayUtils.addAmount(allDistricts, new District("Laboratoire", Category.MERVEILLE, 5), 1);
        ArrayUtils.addAmount(allDistricts, new District("Manoir", Category.NOBLE, 3), 5);
        ArrayUtils.addAmount(allDistricts, new District("Marché", Category.MARCHAND, 2), 4);
        ArrayUtils.addAmount(allDistricts, new District("Monastère", Category.RELIGIEUX, 3), 3);
        ArrayUtils.addAmount(allDistricts, new District("Observatoire", Category.MERVEILLE, 5), 1);
        ArrayUtils.addAmount(allDistricts, new District("Palais", Category.NOBLE, 5), 2);
        ArrayUtils.addAmount(allDistricts, new District("Port", Category.MARCHAND, 4), 3);
        ArrayUtils.addAmount(allDistricts, new District("Prison", Category.MILITAIRE, 2), 3);
        ArrayUtils.addAmount(allDistricts, new District("Taverne", Category.MARCHAND, 1), 5);
        ArrayUtils.addAmount(allDistricts, new District("Temple", Category.RELIGIEUX, 1), 3);
        ArrayUtils.addAmount(allDistricts, new District("Tour de guet", Category.MILITAIRE, 1), 3);
        ArrayUtils.addAmount(allDistricts, new District("Université", Category.MERVEILLE, 6, 8), 1);
        return allDistricts;
    }

    /**
     * Returns the category of this district
     *
     * @return The category of this district
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Get the cost of the district i.e. the amount of gold
     * to pay when building it
     *
     * @return The cost
     */
    public int getCost() {
        return this.cost;
    }

    /**
     * Get the score of the district i.e. the amount of point
     * that the district earns at the end of the game
     *
     * @return The score
     */
    public int getScore() {
        return this.score;
    }

    /**
     * Returns the name of this district
     *
     * @return The name of this district
     */
    public String getName() {
        return this.name;
    }

    @Override
    public String toString() {
        return String.format("%s, %s, prix: %d, score : %d", this.name, this.category, this.cost, this.score);
    }

    @Override
    public boolean equals(Object o) {
        if (o instanceof District d)
            return this.name.equals(d.name) && this.cost == d.cost && this.category.equals(d.category) && this.score == d.score;
        return false;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, category, cost, score);
    }
}