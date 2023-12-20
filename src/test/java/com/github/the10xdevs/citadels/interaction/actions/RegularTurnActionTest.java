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

import java.util.Optional;
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
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
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
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
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
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, Optional<District>> cards = action.drawCards();
                action.chooseCard(cards.first());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, Optional.of(b)));
        cardDrawerBehavior.playTurn(action, view, null);
        assertEquals(a, action.getChosenCard());
        assertEquals(b, action.getDiscardedCard().orElseThrow());
    }

    @Test
    void drawCardsAfterGold() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
                Pair<District, Optional<District>> cards = action.drawCards();
                action.chooseCard(cards.first());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, Optional.of(b)));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void drawCardsInEmptyDeck() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.drawCards();
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        // deck::peekFirstTwo returns a Pair with values (null, null) when deck is empty
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(null, null));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void chooseTwice() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, Optional<District>> cards = action.drawCards();
                action.chooseCard(cards.first());
                action.chooseCard(cards.second().orElseThrow());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, Optional.of(b)));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void chooseInvalid() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.drawCards();
                action.chooseCard(new District("Amphi forum", Category.MERVEILLE, 9));
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, Optional.of(b)));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void chooseNull() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.drawCards();
                action.chooseCard(null);
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        SelfPlayerView view = new SelfPlayerView(cardDrawer);
        RegularTurnAction action = new RegularTurnAction(view, new Pair<>(a, Optional.empty()));
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, view, null));
    }

    @Test
    void buildDistrict() throws IllegalActionException {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
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
    void buildTwoDistricts() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
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
    void buildInvalidDistrict() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
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
    void buildDistrictWithoutEnoughGold() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
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

    @Test
    void buildNullDistrict() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(null);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        SelfPlayerView view = new SelfPlayerView(districtBuilder);
        RegularTurnAction action = new RegularTurnAction(view, null);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, view, null));
    }
}