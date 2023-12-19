package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;
import com.github.the10xdevs.citadels.utils.RandomUtils;

import java.util.*;

/**
 * A bot that does random things
 */
public class RandomBehavior implements Behavior {
    private final Random randomGenerator = new Random();

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(availableRoles);
        // pick a random role
        action.pick(RandomUtils.chooseFrom(this.randomGenerator, roles));
        roles.remove(action.getPickedRole());
        // discard a random role
        action.discard(RandomUtils.chooseFrom(this.randomGenerator, roles));
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        // randomly choose to use his role ability
        if (this.randomGenerator.nextBoolean()) {
            // kill a random role other than himself
            if (self.getCurrentRole() == Role.ASSASSIN) {
                AssassinAbilityAction ability = (AssassinAbilityAction) action.getAbilityAction();
                ability.kill(RandomUtils.chooseFrom(this.randomGenerator, Arrays.stream(Role.values()).filter((role -> role != Role.ASSASSIN)).toList()));
            }
            // steal from a random role other than himself and the assassin
            else if (self.getCurrentRole() == Role.VOLEUR) {
                VoleurAbilityAction ability = (VoleurAbilityAction) action.getAbilityAction();
                ability.stealFrom(RandomUtils.chooseFrom(this.randomGenerator, Arrays.stream(Role.values()).filter((role -> role != Role.VOLEUR && role != Role.ASSASSIN)).toList()));
            }
        }

        // randomly choose between taking gold and drawing cards
        if (!action.canDraw() || this.randomGenerator.nextBoolean()) {
            action.takeGold();
        } else {
            Pair<District, District> cards = action.drawCards();

            // pick a random card between the two options
            action.chooseCard(cards.second() == null || this.randomGenerator.nextBoolean() ? cards.first() : cards.second());
        }

        // randomly choose to build a random affordable district
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