package com.github.the10xdevs.citadels.behaviors;

import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.EnumSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class DummyBehaviorTest {
    @Test
    void pickRoleTest() {
        DummyBehavior dummyBehavior = new DummyBehavior();

        // Set up available roles
        Set<Role> availableRoles = EnumSet.noneOf(Role.class);
        availableRoles.add(Role.ROI);
        availableRoles.add(Role.MARCHAND);

        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction();

        // Call pickRole method
        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }
}
