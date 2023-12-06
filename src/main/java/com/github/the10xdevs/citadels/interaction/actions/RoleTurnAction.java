package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Set;

public class RoleTurnAction {
    private Role pickedRole;
    private Role discardedRole;
    private final Set<Role> availableRoles;

    public RoleTurnAction(Set<Role> availableRoles) {
        this.availableRoles = availableRoles;
    }

    /**
     * Pick this role
     * @param role The role to pick
     */
    public void pick(Role role) throws IllegalActionException {
        if (!availableRoles.contains(role))
            throw new IllegalActionException("Cannot pick role that is not offered");
        if (role.equals(discardedRole))
            throw new IllegalActionException("Picked role is the same as discarded role");
        this.pickedRole = role;
    }

    /**
     * Discard this role
     * @param role The role to discard
     */
    public void discard(Role role) throws IllegalActionException {
        if (!availableRoles.contains(role))
            throw new IllegalActionException("Cannot discard role that is not offered");
        if (role.equals(pickedRole))
            throw new IllegalActionException("Discarded role is the same as picked role");
        this.discardedRole = role;
    }

    public Role getPickedRole() {
        return pickedRole;
    }

    public Role getDiscardedRole() {
        return discardedRole;
    }
}
