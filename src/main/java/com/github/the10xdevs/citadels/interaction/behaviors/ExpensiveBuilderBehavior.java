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
 * A bot that tries to build expensive districts
 */
public class ExpensiveBuilderBehavior implements Behavior {
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
        return ExpensiveBuilderBehavior.rolesImportance.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);

        Role roleToPick = ExpensiveBuilderBehavior.getMostImportantRole(roles).orElseThrow();
        action.pick(roleToPick);
        roles.remove(roleToPick);

        Role roleToDiscard = ExpensiveBuilderBehavior.getMostImportantRole(roles).orElseThrow();
        action.discard(roleToDiscard);
    }

    private static final int GOOD_DISTRICT_THRESHOLD = 4;
    private District markedDistrict;

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
        if (markedDistrict != null) {
            // Try to build our marked district
            if (self.getGold() >= markedDistrict.getCost()) {
                action.buildDistrict(markedDistrict);
                markedDistrict = null;
            } else {
                action.takeGold();
            }
        } else {
            Optional<District> bestDistrictInHand = self.getHand()
                    .stream()
                    .filter(district -> district.getScore() >= GOOD_DISTRICT_THRESHOLD)
                    .filter(district -> !self.getCity().getDistricts().contains(district))
                    .max(Comparator.comparingInt(District::getScore));

            // Get the best good district in our hand
            if (bestDistrictInHand.isPresent()) {
                // Mark this district and try to build it the next turn
                this.markedDistrict = bestDistrictInHand.get();
                action.takeGold();
            } else if (action.canDraw()) {
                // Draw cards and choose the best district
                Pair<District, District> cards = action.drawCards();

                District bestDistrict = cards.second() == null || cards.first().getScore() > cards.second().getScore()
                        ? cards.first()
                        : cards.second();
                action.chooseCard(bestDistrict);
            }
        }
    }
}