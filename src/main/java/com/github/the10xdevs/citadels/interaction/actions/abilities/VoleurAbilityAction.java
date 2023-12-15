package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.models.Role;

public class VoleurAbilityAction extends AbilityAction {
    private Role stolenRole;

    public void stealFrom(Role role) throws IllegalActionException {
        if (role == Role.ASSASSIN)
            throw new IllegalActionException("Cannot stole the assassin");

        this.stolenRole = role;
    }

    public Role getStolenRole() {
        return this.stolenRole;
    }
}
