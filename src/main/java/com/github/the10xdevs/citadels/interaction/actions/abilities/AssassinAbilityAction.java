package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.models.Role;

public class AssassinAbilityAction extends AbilityAction {
    private Role killedRole;

    /**
     * Stores the role that the assassin wants to kill
     * @param role Role killed
     */
    public void kill(Role role) {
        this.killedRole = role;
    }

    /**
     * Returns the role killed by the assassin
     * @return The role killed by the assassin
     */
    public Role getKilledRole() {
        return this.killedRole;
    }
}