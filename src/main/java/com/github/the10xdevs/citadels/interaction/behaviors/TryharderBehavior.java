package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.Role;

import java.util.*;

public class TryharderBehavior implements Behavior {
    private static final List<Role> earlyGameRoles = List.of(
            Role.MARCHAND,
            Role.ASSASSIN,
            Role.VOLEUR
    );

    private static final List<Role> allRoles = List.of(
            Role.MARCHAND,
            Role.ASSASSIN,
            Role.VOLEUR,
            Role.ARCHITECTE,
            Role.ROI,
            Role.CONDOTTIERE,
            Role.EVEQUE,
            Role.MAGICIEN
    );

    private static final int TURN_THRESHOLD = 5;

    private static Optional<Role> getMostImportantRole(Set<Role> availableRoles, List<Role> wantedRoles) {
        return wantedRoles.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }

    private static int getNumberOfBuiltDistrictByCategory(Category category, SelfPlayerView self) {
        return (int) self.getCity().getDistricts().stream()
                .filter(district -> district.getCategory().equals(category))
                .count();
    }

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);

        Optional<Role> role;
        if (gameState.getTurn() < TryharderBehavior.TURN_THRESHOLD) {
            role = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.earlyGameRoles);
            if (role.isPresent()) {
                action.pick(role.get());
                roles.remove(role.get());
            }
        } else {
            // TODO
        }

        if (action.getPickedRole() == null) {
            role = TryharderBehavior.allRoles.stream()
                    .filter(roles::contains)
                    .filter(r -> r.getCategory() != null)
                    .min(Comparator.comparingInt(r -> TryharderBehavior.getNumberOfBuiltDistrictByCategory(r.getCategory(), self)));
            if (role.isPresent()) {
                action.pick(role.get());
                roles.remove(role.get());
            } else {
                Role defaultRole = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.allRoles).orElseThrow();
                action.pick(defaultRole);
                roles.remove(defaultRole);
            }
        }

        if (gameState.getPlayers().size() == 2) {
            Role roleToDiscard = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.allRoles).orElseThrow();
            action.discard(roleToDiscard);
        }
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {

    }
}