package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

class RoleTest {

    @ParameterizedTest
    @EnumSource(Role.class)
    void getAbilityAction(Role targetRole) {
        if (targetRole == Role.ASSASSIN) {
            assertInstanceOf(AssassinAbilityAction.class, targetRole.getAbilityAction());
        } else if (targetRole == Role.VOLEUR) {
            assertInstanceOf(VoleurAbilityAction.class, targetRole.getAbilityAction());
        } else {
            assertNull(targetRole.getAbilityAction());
        }
    }
}