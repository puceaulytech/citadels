package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.Collection;
import java.util.Optional;
import java.util.Random;
import java.util.Set;

/**
 * A dummy bot
 */
public class DummyBehavior implements Behavior {
    private static final Random randomGenerator = new Random();

    private static <T> T chooseRandom(Collection<T> collection) {
        int pos = randomGenerator.nextInt(collection.size());

        int index = 0;
        for (T element : collection) {
            if (index == pos) return element;
            index++;
        }

        throw new IllegalStateException("Random position not in bound");
    }

    @Override
    public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
        // Choose a random role and discard a random one
        action.pick(DummyBehavior.chooseRandom(availableRoles));
        action.discard(DummyBehavior.chooseRandom(availableRoles));
    }

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView game) {
        // Always take gold
        action.takeGold();

        // Build the first district we can afford
        Optional<District> toBuild = self.getHand()
                .stream()
                .filter(district -> district.getCost() <= self.getGold())
                .findFirst();
        toBuild.ifPresent(action::buildDistrict);

        // If we are an assassin, kill the King
        if (self.getCurrentRole() == Role.ASSASSIN) {
            AssassinAbilityAction abilityAction = (AssassinAbilityAction) action.getAbilityAction();
            abilityAction.kill(Role.ROI);
        }
    }
}
