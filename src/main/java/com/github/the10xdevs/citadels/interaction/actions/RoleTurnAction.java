package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Set;

/**
 * A class used by Behaviors to store their choices regarding roles
 *
 * @see com.github.the10xdevs.citadels.interaction.behaviors.Behavior
 */
public class RoleTurnAction {
    private final Set<Role> availableRoles;
    private Role pickedRole;
    private Role discardedRole;

    /**
     * Constructs a RoleTurnAction with a set of available roles to choose from
     *
     * @param availableRoles Set of available roles to choose from
     */
    public RoleTurnAction(Set<Role> availableRoles) {
        this.availableRoles = availableRoles;
    }

    /**
     * Pick this role
     *
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
     *
     * @param role The role to discard
     */
    public void discard(Role role) throws IllegalActionException {
        if (!availableRoles.contains(role))
            throw new IllegalActionException("Cannot discard role that is not offered");
        if (role.equals(pickedRole))
            throw new IllegalActionException("Discarded role is the same as picked role");
        this.discardedRole = role;
    }

    /**
     * Returns the role chosen by the player among the available roles
     *
     * @return The role chosen by the player among the available roles
     */
    public Role getPickedRole() {
        return pickedRole;
    }

    /**
     * Returns the role discarded by the player among the available roles
     *
     * @return The role discarded by the player among the available roles
     */
    public Role getDiscardedRole() {
        return discardedRole;
    }
}