package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class RegularTurnActionTest {
    District a = new District("Testing", Category.MARCHAND, 1);
    District b = new District("Another testing", Category.MARCHAND, 1);

    private Player createFakePlayer(Behavior behavior) {
        Player fake = new Player(behavior);
        fake.setCurrentRole(Role.ASSASSIN);
        return fake;
    }

    private Player createRichPlayer(Behavior behavior) {
        Player fake = createFakePlayer(behavior);
        fake.setGold(20);
        fake.getHand().add(a);
        fake.getHand().add(b);
        return fake;
    }

    @Test
    void takeGold() throws IllegalActionException {
        Behavior goldPickerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
            }
        };

        Player goldPicker = createFakePlayer(goldPickerBehavior);
        SelfPlayerView view = new SelfPlayerView(goldPicker);
        RegularTurnAction action = new RegularTurnAction(view, null);

        goldPickerBehavior.playTurn(action, view, null);

        assertEquals(RegularTurnAction.BasicAction.GOLD, action.getBasicAction());
    }

    @Test
    void takeTooMuchGold() throws IllegalActionException {
        Behavior goldPickerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
                action.takeGold();
            }
        };

        Player goldPicker = createFakePlayer(goldPickerBehavior);
        SelfPlayerView view = new SelfPlayerView(goldPicker);
        RegularTurnAction action = new RegularTurnAction(view, null);

        assertThrows(IllegalActionException.class, () -> goldPickerBehavior.playTurn(action, null, null));
    }

    @Test
    void drawCards() throws IllegalActionException {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, District> cards = action.drawCards();
                action.chooseCard(cards.first());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, b));
        cardDrawerBehavior.playTurn(action, view, null);
        assertEquals(a, action.getChosenCard());
        assertEquals(b, action.getDiscardedCard());
    }

    @Test
    void drawCardsAfterGold() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
                Pair<District, District> cards = action.drawCards();
                action.chooseCard(cards.first());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, b));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void chooseTwice() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, District> cards = action.drawCards();
                action.chooseCard(cards.first());
                action.chooseCard(cards.second());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, b));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void chooseInvalid() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, District> cards = action.drawCards();
                action.chooseCard(new District("Amphi forum", Category.MERVEILLE, 9));
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, b));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void buildDistrict() throws IllegalActionException {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        SelfPlayerView view = new SelfPlayerView(districtBuilder);
        RegularTurnAction action = new RegularTurnAction(view, null);
        districtBuilderBehavior.playTurn(action, view, null);
        assertEquals(a, action.getBuiltDistrict());
    }

    @Test
    void buildTwoDistricts() throws IllegalActionException {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
                action.buildDistrict(b);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        SelfPlayerView view = new SelfPlayerView(districtBuilder);
        RegularTurnAction action = new RegularTurnAction(view, null);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, view, null));
    }

    @Test
    void buildInvalidDistrict() throws IllegalActionException {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(new District("Gold generator", Category.MERVEILLE, 10));
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        SelfPlayerView view = new SelfPlayerView(districtBuilder);
        RegularTurnAction action = new RegularTurnAction(view, null);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, view, null));
    }

    @Test
    void buildDistrictWithoutEnoughGold() throws IllegalActionException {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        districtBuilder.setGold(0);
        SelfPlayerView view = new SelfPlayerView(districtBuilder);
        RegularTurnAction action = new RegularTurnAction(view, null);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, view, null));
    }
}