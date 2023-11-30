package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.List;

public class RegularTurnAction {
    private final AbilityAction abilityAction;

    public RegularTurnAction(AbilityAction abilityAction) {
        this.abilityAction = abilityAction;
    }

    public void takeGold() {
        // TODO: do something
    }

    public List<District> drawCards() {
        // TODO: do something
        return List.of(null, null);
    }

    public void chooseCard(District district) {
        // TODO: do something
    }

    public void buildDistrict(District district) {
        // TODO: do something
    }

    public AbilityAction getAbilityAction() {
        return this.abilityAction;
    }
}
