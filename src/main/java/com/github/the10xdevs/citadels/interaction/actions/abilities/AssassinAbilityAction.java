package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.models.Role;

public class AssassinAbilityAction extends AbilityAction {
    private Role killedRole;

    public void kill(Role role) {
        this.killedRole = role;
    }

    public Role getKilledRole() {
        return this.killedRole;
    }
}
