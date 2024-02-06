package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
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
            Role.VOLEUR,
            Role.ARCHITECTE,
            Role.ROI,
            Role.CONDOTTIERE,
            Role.MARCHAND,
            Role.EVEQUE,
            Role.ASSASSIN,
            Role.MAGICIEN
    );
    private static final int GOOD_DISTRICT_THRESHOLD = 4;
    private District markedDistrict;

    /**
     * Returns the most important role from the available roles.
     *
     * @param availableRoles the roles available to be picked
     * @return the most important role, if any
     */

    private static Optional<Role> getMostImportantRole(Set<Role> availableRoles) {
        return ExpensiveBuilderBehavior.rolesImportance.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }

    /**
     * Picks the most important role from the available roles and discards the most important role if the game has two players.
     *
     * @param action         the RoleTurnAction to be performed
     * @param self           the SelfPlayerView of the current player
     * @param gameState      the current state of the game
     * @param availableRoles the roles available to be picked
     * @throws IllegalActionException if an illegal action is performed
     */

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);

        Role roleToPick = ExpensiveBuilderBehavior.getMostImportantRole(roles).orElseThrow();
        action.pick(roleToPick);
        roles.remove(roleToPick);

        if (gameState.getPlayers().size() == 2) {
            Role roleToDiscard = ExpensiveBuilderBehavior.getMostImportantRole(roles).orElseThrow();
            action.discard(roleToDiscard);
        }
    }

    /**
     * Performs a turn action. The bot always takes gold if it has a district to build.
     * Otherwise, it draws a card and chooses the best district.
     * If it has the role thief, it steals from the king.
     *
     * @param action the RegularTurnAction to be performed
     * @param self   the SelfPlayerView of the current player
     * @param game   the current state of the game
     * @throws IllegalActionException if an illegal action is performed
     */

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
        if (markedDistrict != null && self.getHand().contains(markedDistrict)) {
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
                Pair<District, Optional<District>> cards = action.drawCards();
                District bestDistrict;

                District firstCard = cards.first();
                Optional<District> secondCard = cards.second();

                if (secondCard.isEmpty() || firstCard.getScore() > secondCard.get().getScore()) {
                    bestDistrict = cards.first();
                } else {
                    bestDistrict = secondCard.get();
                }

                action.chooseCard(bestDistrict);
            }
        }

        Optional<Role> killedRole = game.getKilledRole();

        // If it has role thief, steals from role king
        if (self.getCurrentRole() == Role.VOLEUR && (killedRole.isEmpty() || killedRole.get() != Role.ROI)) {
            VoleurAbilityAction ability = (VoleurAbilityAction) action.getAbilityAction();
            ability.stealFrom(Role.ROI);
        }
    }
}