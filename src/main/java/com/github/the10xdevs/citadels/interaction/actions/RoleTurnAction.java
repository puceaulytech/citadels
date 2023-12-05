package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.models.Role;

public class RoleTurnAction {
    private Role pickedRole;
    private Role discardedRole;

    /**
     * Pick this role
     * @param role The role to pick
     */
    public void pick(Role role) {
        this.pickedRole = role;
    }

    /**
     * Discard this role
     * @param role The role to discard
     */
    public void discard(Role role) {
        this.discardedRole = role;
    }

    public Role getPickedRole() {
        return pickedRole;
    }

    public Role getDiscardedRole() {
        return discardedRole;
    }
}
