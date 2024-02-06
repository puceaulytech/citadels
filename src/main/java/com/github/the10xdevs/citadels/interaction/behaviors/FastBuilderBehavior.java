package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;

import java.util.EnumSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

/**
 * A bot that tries to build as fast as possible
 */
public class FastBuilderBehavior implements Behavior {
    private static final List<Role> rolesImportance = List.of(
            Role.ROI,
            Role.CONDOTTIERE,
            Role.MARCHAND,
            Role.EVEQUE,
            Role.ASSASSIN,
            Role.VOLEUR,
            Role.MAGICIEN,
            Role.ARCHITECTE
    );
    private Role previousRole;

    /**
     * Returns the most important role from the available roles.
     *
     * @param availableRoles the roles available to be picked
     * @return the most important role, if any
     */

    private static Optional<Role> getMostImportantRole(Set<Role> availableRoles) {
        return FastBuilderBehavior.rolesImportance.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }

    /**
     * Returns the most important role from the available roles.
     *
     * @param availableRoles the roles available to be picked
     * @return the most important role, if any
     */

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);
        if (this.previousRole != null)
            roles.remove(this.previousRole);

        Role roleToPick = FastBuilderBehavior.getMostImportantRole(roles).orElseThrow();
        action.pick(roleToPick);
        roles.remove(roleToPick);

        if (gameState.getPlayers().size() == 2) {
            if (availableRoles.contains(this.previousRole))
                roles.add(this.previousRole);
            Role roleToDiscard = FastBuilderBehavior.getMostImportantRole(roles).orElseThrow();
            action.discard(roleToDiscard);
        }

        this.previousRole = roleToPick;
    }

    /**
     * Performs a turn action. The bot always draws a card until its hand size reaches eight, then it always takes gold.
     * It also tries to build the first district it can afford.
     *
     * @param action the RegularTurnAction to be performed
     * @param self   the SelfPlayerView of the current player
     * @param game   the current state of the game
     * @throws IllegalActionException if an illegal action is performed
     */

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {

        // Always Draw a card until HandSize equals eight
        if (action.canDraw() && self.getHandSize() < 8) {
            Pair<District, Optional<District>> cards = action.drawCards();
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