package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.MagicienAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;

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
            Role.EVEQUE,
            Role.MAGICIEN,
            Role.ARCHITECTE,
            Role.CONDOTTIERE,
            Role.ROI
    );

    private static final int TURN_THRESHOLD = 3;
    private static final int GOLD_THRESHOLD = 5;
    private static final int SCORE_THRESHOLD = 4;

    private int turnsWithoutBuilding = 0;

    /**
     * This method returns the most important role from a set of available roles.
     * The importance of a role is determined by its position in the list of wanted roles.
     * @param availableRoles The set of available roles.
     * @param wantedRoles The list of wanted roles, ordered by importance.
     * @return The most important role from the set of available roles.
     */
    private static Optional<Role> getMostImportantRole(Set<Role> availableRoles, List<Role> wantedRoles) {
        return wantedRoles.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }
    /**
     * This method returns the number of built districts of a certain category.
     * @param category The category of the districts.
     * @param self The view of the player.
     * @return The number of built districts of the given category.
     */

    private static int getNumberOfBuiltDistrictByCategory(Category category, SelfPlayerView self) {
        return (int) self.getCity().getDistricts().stream()
                .filter(district -> district.getCategory().equals(category))
                .count();
    }
    /**
     * This method compares two districts based on their scores and categories.
     * @param d1 The first district.
     * @param d2 The second district.
     * @param self The view of the player.
     * @param turn The current turn.
     * @return The difference between the scores of the two districts.
     */

    private static int compareCard(District d1, District d2, SelfPlayerView self, int turn) {
        int scoreD1 = TryharderBehavior.getNumberOfBuiltDistrictByCategory(d1.getCategory(), self) == 0 ? 1 : 0;
        int scoreD2 = TryharderBehavior.getNumberOfBuiltDistrictByCategory(d2.getCategory(), self) == 0 ? -1 : 0;
        if (scoreD1 + scoreD2 == 0 || turn > TryharderBehavior.TURN_THRESHOLD * 2)
            return d1.getScore() - d2.getScore();
        return scoreD1 + scoreD2;
    }
    /**
     * This method defines how the player picks a role during their role turn.
     * @param action The role turn action.
     * @param self The view of the player.
     * @param gameState The state of the game.
     * @throws IllegalActionException If the action is illegal.
     */

    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(action.getAvailableRoles());

        Optional<Role> role;
        if (gameState.getTurn() < TryharderBehavior.TURN_THRESHOLD) {
            role = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.earlyGameRoles);
            if (role.isPresent()) {
                action.pick(role.get());
                roles.remove(role.get());
            }
        } else if (self.getGold() >= TryharderBehavior.GOLD_THRESHOLD && roles.contains(Role.ARCHITECTE)) {
            action.pick(Role.ARCHITECTE);
            roles.remove(Role.ARCHITECTE);
        }

        if (action.getPickedRole() == null) {
            role = TryharderBehavior.allRoles.stream()
                    .filter(roles::contains)
                    .filter(r -> r.getCategory() != null)
                    .filter(r -> TryharderBehavior.getNumberOfBuiltDistrictByCategory(r.getCategory(), self) != 0)
                    .max(Comparator.comparingInt(r -> TryharderBehavior.getNumberOfBuiltDistrictByCategory(r.getCategory(), self)));
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

    /**
     * This method defines how the player plays their regular turn.
     * @param action The regular turn action.
     * @param self The view of the player.
     * @param gameState The state of the game.
     * @throws IllegalActionException If the action is illegal.
     */

    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        int firstScoreCalculation = self.getCity().getDistricts().size() >= 4 ? TryharderBehavior.SCORE_THRESHOLD : TryharderBehavior.SCORE_THRESHOLD / 2;
        int currentScoreThreshold = turnsWithoutBuilding > TryharderBehavior.TURN_THRESHOLD ? 0 : firstScoreCalculation;

        if (self.getGold() < TryharderBehavior.GOLD_THRESHOLD) {
            action.takeGold();
        }

        Optional<District> toBuild = self.getHand().stream()
                .filter(district -> district.getScore() >= currentScoreThreshold)
                .filter(district -> !self.getCity().getDistricts().contains(district))
                .max((d1, d2) -> {
                    if (TryharderBehavior.getNumberOfBuiltDistrictByCategory(d1.getCategory(), self) == 0)
                        return 1;
                    if (TryharderBehavior.getNumberOfBuiltDistrictByCategory(d2.getCategory(), self) == 0)
                        return -1;
                    return 0;
                });

        if (toBuild.isPresent() && toBuild.get().getCost() <= self.getGold()) {
            action.buildDistrict(toBuild.get());
            this.turnsWithoutBuilding = 0;
        } else {
            this.turnsWithoutBuilding++;
        }

        if (action.getBasicAction() != RegularTurnAction.BasicAction.GOLD && action.canDraw()) {
            Pair<District, Optional<District>> cards = action.drawCards();

            Optional<District> second = cards.second();
            if (second.isPresent())
                action.chooseCard(
                        TryharderBehavior.compareCard(cards.first(), second.get(), self, gameState.getTurn()) > 0
                                ? cards.first()
                                : second.get()
                );
            else
                action.chooseCard(cards.first());
        }

        if (action.getBasicAction() == null)
            action.takeGold();

        switch (self.getCurrentRole()) {
            case ASSASSIN: {
                AssassinAbilityAction ability = (AssassinAbilityAction) action.getAbilityAction();
                if (!gameState.getRolesFacingUp().contains(Role.MARCHAND))
                    ability.kill(Role.MARCHAND);
                else
                    ability.kill(Role.MAGICIEN);
                break;
            }
            case VOLEUR: {
                VoleurAbilityAction ability = (VoleurAbilityAction) action.getAbilityAction();
                Role roleToSteal = TryharderBehavior.allRoles.stream()
                        .filter(role -> role != Role.ASSASSIN)
                        .filter(role -> gameState.getKilledRole().isEmpty() || role != gameState.getKilledRole().get())
                        .filter(role -> role != self.getCurrentRole())
                        .filter(role -> !gameState.getRolesFacingUp().contains(role))
                        .findFirst().orElseThrow();
                ability.stealFrom(roleToSteal);
                break;
            }
            case MAGICIEN: {
                TryharderBehavior.handleMagicianFengShui(action, self, gameState, currentScoreThreshold);
                break;
            }
            case ARCHITECTE: {
                // TODO
                break;
            }
            default:
        }
    }

    private static void handleMagicianFengShui(RegularTurnAction action, SelfPlayerView self, GameView gameState, int currentScoreThreshold) throws IllegalActionException {
        MagicienAbilityAction ability = (MagicienAbilityAction) action.getAbilityAction();
        PlayerView playerWithMostCards = gameState.getPlayers().stream()
                .filter(player -> !self.equals(player))
                .max(Comparator.comparingInt(PlayerView::getHandSize))
                .orElseThrow();
        if (playerWithMostCards.getHandSize() > self.getHandSize() * 2)
            ability.exchangeHandWith(playerWithMostCards);

        else if (gameState.getDeckSize() > 0) {
            double averageScoreInHand = (double) self.getHand().stream()
                    .mapToInt(District::getScore)
                    .count() / self.getHandSize();
            if (averageScoreInHand < currentScoreThreshold) {
                List<District> cardsToDiscard = self.getHand().stream()
                        .sorted(Comparator.comparingInt(District::getScore).reversed())
                        .limit(gameState.getDeckSize())
                        .toList();
                ability.discardAndDraw(cardsToDiscard);
            }
        }
    }
}