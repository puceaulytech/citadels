package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.models.District;

import java.util.List;

public class RegularTurnAction {
    private final AbilityAction abilityAction;

    private BasicAction basicAction;

    public RegularTurnAction(AbilityAction abilityAction) {
        this.abilityAction = abilityAction;
    }

    public void takeGold() throws IllegalActionException {
        if (this.basicAction != null)
            throw new IllegalActionException("Cannot take gold because an action has already been performed");
        this.basicAction = BasicAction.GOLD;
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

    public BasicAction getBasicAction() {
        return this.basicAction;
    }

    public enum BasicAction {
        GOLD,
        CARDS,
    }
}
