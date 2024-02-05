package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.interaction.actions.abilities.*;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;

import static org.junit.jupiter.api.Assertions.*;

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
        } else if (targetRole == Role.ARCHITECTE) {
            assertInstanceOf(ArchitecteAbilityAction.class, targetRole.getAbilityAction(null, null));
        } else {
            assertNull(targetRole.getAbilityAction(null, null));
        }
    }

    @Test
    void getTurnOrder() {
        assertEquals(1, Role.ASSASSIN.getTurnOrder());
        assertEquals(2, Role.VOLEUR.getTurnOrder());
        assertEquals(3, Role.MAGICIEN.getTurnOrder());
        assertEquals(4, Role.ROI.getTurnOrder());
        assertEquals(5, Role.EVEQUE.getTurnOrder());
        assertEquals(6, Role.MARCHAND.getTurnOrder());
        assertEquals(7, Role.ARCHITECTE.getTurnOrder());
        assertEquals(8, Role.CONDOTTIERE.getTurnOrder());
    }

    @Test
    void getCategory() {
        assertNull(Role.ASSASSIN.getCategory());
        assertNull(Role.VOLEUR.getCategory());
        assertNull(Role.ARCHITECTE.getCategory());
        assertNotNull(Role.CONDOTTIERE.getCategory());

        assertEquals(Category.NOBLE, Role.ROI.getCategory());
        assertEquals(Category.RELIGIEUX, Role.EVEQUE.getCategory());
        assertEquals(Category.MARCHAND, Role.MARCHAND.getCategory());
    }
}