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

public class Player {
    private final Behavior behavior;
    private final City city = new City();
    private final List<District> hand = new ArrayList<>();
    private int gold = 0;
    private Role currentRole;

    public Player(Behavior behavior) {
        this.behavior = behavior;
    }

    public Behavior getBehavior() {
        return behavior;
    }

    public int getGold() {
        return gold;
    }

    public void setGold(int gold) {
        this.gold = gold;
    }

    public void incrementGold(int amount) {
        this.gold += amount;
    }

    public City getCity() {
        return city;
    }

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

    public Role getCurrentRole() {
        return currentRole;
    }

    public void setCurrentRole(Role currentRole) {
        this.currentRole = currentRole;
    }

    public List<District> getHand() {
        return this.hand;
    }
}
