package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.City;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * A wrapper for all the player information
 * <br>
 * This class does not handle the decision-making and is supposed to be used by Game only
 */
public class Player {
    private final Behavior behavior;
    private final City city = new City();
    private final List<District> hand = new ArrayList<>();
    private int gold = 0;
    private Role currentRole;

    /**
     * Constructs a Player with a specific behavior
     * @param behavior The player behavior for the decision-making
     */
    public Player(Behavior behavior) {
        this.behavior = behavior;
    }

    /**
     * Returns the player behavior
     * @return The player behavior
     */
    public Behavior getBehavior() {
        return behavior;
    }

    /**
     * Returns the player's gold amount
     * @return The player's gold amount
     */
    public int getGold() {
        return gold;
    }

    /**
     * Sets the player's gold amount
     * @param gold The amount of gold to set
     */
    public void setGold(int gold) {
        this.gold = gold;
    }

    /**
     * Increases the player's gold amount by a certain amount
     * @param amount The amount of gold to add
     */
    public void incrementGold(int amount) {
        this.gold += amount;
    }

    /**
     * Returns the player's city
     * @return The player's city
     */
    public City getCity() {
        return city;
    }

    /**
     * Computes and returns the player's score according to its built districts
     * @param wasFirstPlayer If the player was the first one to build 8 districts
     * @return The player's score
     */
    public int getScore(boolean wasFirstPlayer) {
        // Sum of the score of all the districts in the city
        int score = this.getCity().getDistricts().stream().mapToInt(District::getScore).sum();

        // If the player has a district of each category, he earns
        Set<Category> districtsCategories = this.getCity().getDistricts()
                .stream()
                .map(District::getCategory)
                .collect(Collectors.toSet());

        if (districtsCategories.size() == 5)
            score += 3;

        if (this.getCity().getSize() == 8)
            score += wasFirstPlayer ? 4 : 2;

        return score;
    }

    /**
     * Returns the player's current chosen role
     * @return The player's current chosen role
     */
    public Role getCurrentRole() {
        return currentRole;
    }

    /**
     * Sets the player's chosen role
     * @param currentRole The player's chosen role
     */
    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    /**
     * Returns the player's hand
     * @return The player's hand
     */
    public List<District> getHand() {
        return this.hand;
    }
}