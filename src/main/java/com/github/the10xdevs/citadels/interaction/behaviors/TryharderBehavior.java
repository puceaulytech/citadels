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
    // Best roles for it in an early games are these roles
    private static final List<Role> earlyGameRoles = List.of(
            Role.MARCHAND,
            Role.ASSASSIN,
            Role.VOLEUR
    );

    // And these are all the roles ranked by its preference
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

    // Some constants to dictate the way it plays
    private static final int TURN_THRESHOLD = 3;
    private static final int GOLD_THRESHOLD = 5;
    private static final int SCORE_THRESHOLD = 4;

    // Counter of turns spent without building any district
    private int turnsWithoutBuilding = 0;

    /**
     * This method returns the most important role from a set of available roles.
     * The importance of a role is determined by its position in the list of wanted roles.
     *
     * @param availableRoles The set of available roles.
     * @param wantedRoles    The list of wanted roles, ordered by importance.
     * @return The most important role from the set of available roles.
     */
    private static Optional<Role> getMostImportantRole(Set<Role> availableRoles, List<Role> wantedRoles) {
        return wantedRoles.stream()
                .filter(availableRoles::contains)
                .findFirst();
    }

    /**
     * This method returns the number of built districts of a certain category.
     *
     * @param category The category of the districts.
     * @param self     The view of the player.
     * @return The number of built districts of the given category.
     */
    private static int getNumberOfBuiltDistrictByCategory(Category category, SelfPlayerView self) {
        return (int) self.getCity().getDistricts().stream()
                .filter(district -> district.getCategory().equals(category))
                .count();
    }

    /**
     * This method compares two districts based on their scores and categories.
     * This bot finds a card interesting if it hasn't built any district of its category
     * and if the categories are as interesting OR if the game is kind of early, it chooses the one with the highest score.
     *
     * @param d1   The first district.
     * @param d2   The second district.
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
     * Handles how the player plays the Magician role
     *
     * @param action                The action
     * @param self                  The view of the current player
     * @param gameState             The view of the current game
     * @param currentScoreThreshold The number that determines how picky the player will be regarding district scores
     * @throws IllegalActionException If an illegal action is performed
     */
    private static void handleMagicianFengShui(RegularTurnAction action, SelfPlayerView self, GameView gameState, int currentScoreThreshold) throws IllegalActionException {
        MagicienAbilityAction ability = (MagicienAbilityAction) action.getAbilityAction();
        // Get the player that has the most cards
        PlayerView playerWithMostCards = gameState.getPlayers().stream()
                .filter(player -> !self.equals(player))
                .max(Comparator.comparingInt(PlayerView::getHandSize))
                .orElseThrow();
        // If he has twice as many cards as us then swap cards with him
        if (playerWithMostCards.getHandSize() > self.getHandSize() * 2)
            ability.exchangeHandWith(playerWithMostCards);

            // If it is not interesting to swap cards with a player
        else if (gameState.getDeckSize() > 0) {
            double averageScoreInHand = (double) self.getHand().stream()
                    .mapToInt(District::getScore)
                    .sum() / self.getHandSize();
            // And if the average score of our hand is relatively bad
            // Then discard as many cards as the deck offers, ranked by worst score
            if (averageScoreInHand < currentScoreThreshold) {
                List<District> cardsToDiscard = self.getHand().stream()
                        .sorted(Comparator.comparingInt(District::getScore))
                        .limit(gameState.getDeckSize())
                        .toList();
                ability.discardAndDraw(cardsToDiscard);
            }
        }
    }

    private static void useRoleAbility(RegularTurnAction action, SelfPlayerView self, GameView gameState, int currentScoreThreshold) throws IllegalActionException {
        switch (self.getCurrentRole()) {
            case ASSASSIN: {
                // Always kill Merchant if it's not in the cards facing up, and if it is, kill Magician
                AssassinAbilityAction ability = (AssassinAbilityAction) action.getAbilityAction();
                if (!gameState.getRolesFacingUp().contains(Role.MARCHAND))
                    ability.kill(Role.MARCHAND);
                else
                    ability.kill(Role.MAGICIEN);
                break;
            }
            case VOLEUR: {
                // Steal from the first role possible in the allRoles list
                VoleurAbilityAction ability = (VoleurAbilityAction) action.getAbilityAction();
                Optional<Role> roleToSteal = TryharderBehavior.allRoles.stream()
                        .filter(role -> role != Role.ASSASSIN)
                        .filter(role -> gameState.getKilledRole().isEmpty() || role != gameState.getKilledRole().get())
                        .filter(role -> role != self.getCurrentRole())
                        .filter(role -> !gameState.getRolesFacingUp().contains(role))
                        .findFirst();
                if (roleToSteal.isPresent())
                    ability.stealFrom(roleToSteal.get());
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

    /**
     * This method defines how the player picks a role during their role turn.
     *
     * @param action    The role turn action.
     * @param self      The view of the player.
     * @param gameState The state of the game.
     * @throws IllegalActionException If the action is illegal.
     */
    @Override
    public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        Set<Role> roles = EnumSet.copyOf(action.getAvailableRoles());

        Optional<Role> role;
        // If the game is pretty early
        if (gameState.getTurn() < TryharderBehavior.TURN_THRESHOLD) {
            // Try to pick on of the early game roles
            role = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.earlyGameRoles);
            if (role.isPresent()) {
                action.pick(role.get());
                roles.remove(role.get());
            }
            // If the architect is available and the player is rich then choose architect
        } else if (self.getGold() >= TryharderBehavior.GOLD_THRESHOLD && roles.contains(Role.ARCHITECTE)) {
            action.pick(Role.ARCHITECTE);
            roles.remove(Role.ARCHITECTE);
        }

        // If the player still hasn't chosen a role by now
        if (action.getPickedRole() == null) {
            // Then try to pick the one with a category of which the player has some built district
            role = TryharderBehavior.allRoles.stream()
                    .filter(roles::contains)
                    .filter(r -> r.getCategory() != null)
                    .filter(r -> TryharderBehavior.getNumberOfBuiltDistrictByCategory(r.getCategory(), self) != 0)
                    .max(Comparator.comparingInt(r -> TryharderBehavior.getNumberOfBuiltDistrictByCategory(r.getCategory(), self)));
            if (role.isPresent()) {
                action.pick(role.get());
                roles.remove(role.get());
            } else {
                // If no role with a category is available, then pick the preferred one among the available ones
                Role defaultRole = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.allRoles).orElseThrow();
                action.pick(defaultRole);
                roles.remove(defaultRole);
            }
        }

        // Discard the second preferred role if there are only two players
        if (gameState.getPlayers().size() == 2) {
            Role roleToDiscard = TryharderBehavior.getMostImportantRole(roles, TryharderBehavior.allRoles).orElseThrow();
            action.discard(roleToDiscard);
        }
    }

    /**
     * This method defines how the player plays their regular turn.
     *
     * @param action    The regular turn action.
     * @param self      The view of the player.
     * @param gameState The state of the game.
     * @throws IllegalActionException If the action is illegal.
     */
    @Override
    public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
        // Determine how strict the player will be regarding district scores
        // If it has previously built four districts then it will be pretty picky
        // If it hasn't built any district for the past X turns then it will try to build anything
        int firstScoreCalculation = self.getCity().getDistricts().size() >= 4 ? TryharderBehavior.SCORE_THRESHOLD : TryharderBehavior.SCORE_THRESHOLD / 2;
        int currentScoreThreshold = turnsWithoutBuilding > TryharderBehavior.TURN_THRESHOLD ? 0 : firstScoreCalculation;

        if (self.getGold() < TryharderBehavior.GOLD_THRESHOLD) {
            action.takeGold();
        }

        // Try to build a district to have one of each category and with a relatively high score
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

        // If it has enough gold and the deck is not empty
        if (action.getBasicAction() != RegularTurnAction.BasicAction.GOLD && action.canDraw()) {
            Pair<District, Optional<District>> cards = action.drawCards();

            // Choose the best card according to score and category (see method compareCard)
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

        // If nothing has been done so far, then take gold
        if (action.getBasicAction() == null)
            action.takeGold();

        TryharderBehavior.useRoleAbility(action, self, gameState, currentScoreThreshold);
    }
}