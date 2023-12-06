package com.github.the10xdevs.citadels.behaviors;

import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.*;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class DummyBehaviorTest {
    private static Stream<Set<Role>> generateRoles() {
        Role[] roles = Role.values();
        List<Set<Role>> allRoles = new ArrayList<>();

        for (int i = 2; i <= Role.values().length; i++) {
            generateCombinations(roles, allRoles, EnumSet.noneOf(Role.class), 0, i);
        }

        return allRoles.stream();
    }

    private static void generateCombinations(Role[] roles, List<Set<Role>> allCombinations, Set<Role> current, int start, int length) {
        if (length == 0) {
            allCombinations.add(EnumSet.copyOf(current));
            return;
        }

        for (int i = start; i <= roles.length - length; i++) {
            current.add(roles[i]);
            generateCombinations(roles, allCombinations, current, i + 1, length - 1);
            current.remove(roles[i]);
        }
    }

    @ParameterizedTest
    @MethodSource("generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        DummyBehavior dummyBehavior = new DummyBehavior();

        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }
}
