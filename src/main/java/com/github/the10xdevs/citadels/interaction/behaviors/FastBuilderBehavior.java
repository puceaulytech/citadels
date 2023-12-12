package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;

import java.util.*;

/**
 * A dummy bot
 */
public class FastBuilderBehavior implements Behavior {
    private static final List<Role> rolesImportance = List.of(
            Role.ROI,
            Role.CONDOTTIERE,
            Role.VOLEUR,
            Role.MARCHAND,
            Role.EVEQUE,
            Role.ASSASSIN,
            Role.MAGICIEN,
            Role.ARCHITECTE
    );

    private static Optional<Role> getMostImportantRole(Set<Role> availableRoles) {
        return FastBuilderBehavior.rolesImportance.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);

        Role roleToPick = FastBuilderBehavior.getMostImportantRole(roles).orElseThrow();
        action.pick(roleToPick);
        roles.remove(roleToPick);

        Role roleToDiscard = FastBuilderBehavior.getMostImportantRole(roles).orElseThrow();
        action.discard(roleToDiscard);
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {

        // Always Draw a card until HandSize equals eight
        if (action.canDraw() && self.getHandSize() < 8) {
            Pair<District, District> cards = action.drawCards();
            action.chooseCard(cards.first());
        } else {
            // Always take gold after reaching eight card in hand
            action.takeGold();
        }

        // Build the first district we can afford
        Optional<District> toBuild = self.getHand()
                .stream()
                .filter(district -> !self.getCity().getDistricts().contains(district))
                .filter(district -> district.getCost() <= self.getGold())
                .findFirst();
        if (toBuild.isPresent())
            action.buildDistrict(toBuild.get());
    }
}
