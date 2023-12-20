package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;
import com.github.the10xdevs.citadels.utils.RandomUtils;

import java.util.*;
import java.util.stream.Stream;

/**
 * A bot that does random things
 */
public class RandomBehavior implements Behavior {
    private final Random randomGenerator = new Random();

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);
        action.pick(RandomUtils.chooseFrom(this.randomGenerator, roles));
        roles.remove(action.getPickedRole());
        action.discard(RandomUtils.chooseFrom(this.randomGenerator, roles));
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        if (!action.canDraw() || this.randomGenerator.nextBoolean()) {
            action.takeGold();
        } else {
            Pair<District, Optional<District>> cards = action.drawCards();

            action.chooseCard(cards.second().isEmpty() || this.randomGenerator.nextBoolean() ? cards.first() : cards.second().get());
        }

        if (this.randomGenerator.nextBoolean()) {
            List<District> availableDistricts = self.getHand().stream()
                    .filter(district -> self.getGold() >= district.getCost())
                    .filter(district -> !self.getCity().getDistricts().contains(district))
                    .toList();

            if (!availableDistricts.isEmpty()) {
                District districtToBuild = RandomUtils.chooseFrom(this.randomGenerator, availableDistricts);

                action.buildDistrict(districtToBuild);
            }
        }
    }
}
