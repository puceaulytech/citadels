package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.CondottiereAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
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

    /**
     * Picks a random role from the available roles and discards a random role if the game has two players.
     *
     * @param action    the RoleTurnAction to be performed
     * @param self      the SelfPlayerView of the current player
     * @param gameState the current state of the game
     * @throws IllegalActionException if an illegal action is performed
     */

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(action.getAvailableRoles());
        // pick a random role
        action.pick(RandomUtils.chooseFrom(this.randomGenerator, roles));
        roles.remove(action.getPickedRole());
        // discard a random role
        if (gameState.getPlayers().size() == 2) {
            action.discard(RandomUtils.chooseFrom(this.randomGenerator, roles));
        }
    }

    /**
     * Performs a random turn action. The bot may choose to use its role ability, take gold, draw cards, or build a district.
     *
     * @param action    the RegularTurnAction to be performed
     * @param self      the SelfPlayerView of the current player
     * @param gameState the current state of the game
     * @throws IllegalActionException if an illegal action is performed
     */

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        // randomly choose to use his role ability
        if (this.randomGenerator.nextBoolean()) {
            // kill a random role other than himself
            if (self.getCurrentRole() == Role.ASSASSIN) {
                this.handleAssassinAbility(action);
            }
            // steal from a random role other than himself and the assassin
            else if (self.getCurrentRole() == Role.VOLEUR) {
                this.handleVoleurAbility(action, gameState);
            } else if (self.getCurrentRole() == Role.CONDOTTIERE) {
                this.handleCondottiereAbility(action, self, gameState);
            }
        }

        // randomly choose between taking gold and drawing cards
        if (!action.canDraw() || this.randomGenerator.nextBoolean()) {
            action.takeGold();
        } else {
            Pair<District, Optional<District>> cards = action.drawCards();

            // pick a random card between the two options
            Optional<District> second = cards.second();
            action.chooseCard(second.isEmpty() || this.randomGenerator.nextBoolean() ? cards.first() : second.get());
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

    private void handleCondottiereAbility(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        CondottiereAbilityAction ability = (CondottiereAbilityAction) action.getAbilityAction();

        List<PlayerView> targetPlayers = gameState.getPlayers()
                .stream()
                .filter(player -> player.getCurrentRole() != Role.EVEQUE)
                .filter(player -> player.getCity().getDistricts().size() != 8)
                .toList();

        PlayerView targetPlayer = RandomUtils.chooseFrom(this.randomGenerator, targetPlayers);

        if (targetPlayer != null) {
            List<District> targetDistricts = targetPlayer.getCity().getDistricts()
                    .stream()
                    .filter(district -> !district.getName().equals("Donjon"))
                    .filter(district -> district.getCost() - 1 <= self.getGold())
                    .toList();

            if (!targetDistricts.isEmpty()) {
                District targetDistrict = RandomUtils.chooseFrom(this.randomGenerator, targetDistricts);

                ability.destroy(targetPlayer, targetDistrict);
            }
        }
    }

    private void handleVoleurAbility(RegularTurnAction action, GameView gameState) throws IllegalActionException {
        VoleurAbilityAction ability = (VoleurAbilityAction) action.getAbilityAction();

        List<Role> rolesToSteal = Arrays.stream(Role.values())
                .filter(role -> role != Role.VOLEUR)
                .filter(role -> role != Role.ASSASSIN)
                .filter(role -> gameState.getKilledRole().isEmpty() || gameState.getKilledRole().get() != role)
                .toList();

        ability.stealFrom(RandomUtils.chooseFrom(this.randomGenerator, rolesToSteal));
    }

    private void handleAssassinAbility(RegularTurnAction action) throws IllegalActionException {
        AssassinAbilityAction ability = (AssassinAbilityAction) action.getAbilityAction();

        List<Role> rolesToKill = Arrays.stream(Role.values())
                .filter(role -> role != Role.ASSASSIN)
                .toList();

        ability.kill(RandomUtils.chooseFrom(this.randomGenerator, rolesToKill));
    }
}