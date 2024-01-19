package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.CondottiereAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.MagicienAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;

class RoleTest {

    @ParameterizedTest
    @EnumSource(Role.class)
    void getAbilityAction(Role targetRole) {
        if (targetRole == Role.ASSASSIN) {
            assertInstanceOf(AssassinAbilityAction.class, targetRole.getAbilityAction(null, null));
        } else if (targetRole == Role.VOLEUR) {
            assertInstanceOf(VoleurAbilityAction.class, targetRole.getAbilityAction(null, null));
        } else if (targetRole == Role.MAGICIEN) {
            assertInstanceOf(MagicienAbilityAction.class, targetRole.getAbilityAction(null, null));
        } else if (targetRole == Role.CONDOTTIERE) {
            assertInstanceOf(CondottiereAbilityAction.class, targetRole.getAbilityAction(null, null));
        } else {
            assertNull(targetRole.getAbilityAction(null, null));
        }
    }
}