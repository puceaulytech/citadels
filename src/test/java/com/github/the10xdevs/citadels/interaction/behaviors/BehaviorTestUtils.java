package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.models.Role;

import java.util.ArrayList;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Stream;

public class BehaviorTestUtils {
    public static Stream<Set<Role>> generateRoles() {
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
}
