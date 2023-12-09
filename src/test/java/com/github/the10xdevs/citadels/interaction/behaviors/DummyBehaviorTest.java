package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class DummyBehaviorTest {

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        DummyBehavior dummyBehavior = new DummyBehavior();

        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, null, null, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }
}
