package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RandomBehaviorTest {
    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        RandomBehavior dummyBehavior = new RandomBehavior();

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
