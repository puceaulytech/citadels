package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.models.Role;

public class VoleurAbilityAction extends AbilityAction {
    private Role stolenRole;

    /**
     * Stores the role that the thief wants to steal from
     * @param role Role robbed
     * @throws IllegalActionException If the thief tried to steal from the assassin
     */
    public void stealFrom(Role role) throws IllegalActionException {
        if (role == Role.ASSASSIN)
            throw new IllegalActionException("Cannot steal from the assassin");

        this.stolenRole = role;
    }

    /**
     * Returns the role stolen from
     * @return The role stolen from
     */
    public Role getStolenRole() {
        return this.stolenRole;
    }
}
