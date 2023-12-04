package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
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
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
        // Always take gold
        action.takeGold();

        // Always Draw a card until HandSize equals eight
        if (self.getHandSize() < 8) {
            action.drawCards();
        }

        // Build the first district we can afford
        Optional<District> toBuild = self.getHand()
                .stream()
                .filter(district -> district.getCost() <= self.getGold())
                .findFirst();
        toBuild.ifPresent(action::buildDistrict);
    }
}
