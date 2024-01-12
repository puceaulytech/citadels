package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.models.Role;

public class AssassinAbilityAction extends AbilityAction {
    private Role killedRole;

    public AssassinAbilityAction(Game game) {
        super(null, game);
    }

    /**
     * Stores the role that the assassin wants to kill
     *
     * @param role Role killed
     */
    public void kill(Role role) throws IllegalActionException {
        if (role == null)
            throw new IllegalActionException("No role provided");
        this.killedRole = role;
        this.game.setKilledRole(role);
    }

    /**
     * Returns the role killed by the assassin
     *
     * @return The role killed by the assassin
     */
    public Role getKilledRole() {
        return this.killedRole;
    }
}