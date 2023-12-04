package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.models.City;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.ArrayList;
import java.util.List;

public class Player {
    private final Behavior behavior;
    private final City city = new City();
    private int gold = 0;
    private Role currentRole;
    private final List<District> hand = new ArrayList<>();

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
