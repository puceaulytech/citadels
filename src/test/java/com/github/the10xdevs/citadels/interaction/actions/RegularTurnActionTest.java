package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.abilities.ArchitecteAbilityAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

class RegularTurnActionTest {
    District a = new District("Testing", Category.MARCHAND, 1);
    District b = new District("Another testing", Category.MARCHAND, 1);
    GameBuilder gameBuilder;

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

    @BeforeEach
    void initGameBuilder() {
        gameBuilder = GameBuilder.create();
    }

    @Test
    void takeGold() throws IllegalActionException {
        Behavior goldPickerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
            }
        };

        Player goldPicker = createFakePlayer(goldPickerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), goldPicker);

        goldPickerBehavior.playTurn(action, new SelfPlayerView(goldPicker), null);

        assertEquals(RegularTurnAction.BasicAction.GOLD, action.getBasicAction());
    }

    @Test
    void takeTooMuchGold() {
        Behavior goldPickerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
                action.takeGold();
            }
        };

        Player goldPicker = createFakePlayer(goldPickerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), goldPicker);

        assertThrows(IllegalActionException.class, () -> goldPickerBehavior.playTurn(action, null, null));
    }

    @Test
    void drawCards() throws IllegalActionException {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, Optional<District>> cards = action.drawCards();
                action.chooseCard(cards.first());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>(List.of(a, b))).build(), cardDrawer);
        cardDrawerBehavior.playTurn(action, new SelfPlayerView(cardDrawer), null);
        assertEquals(a, action.getChosenCard());
        assertEquals(b, action.getDiscardedCard().orElseThrow());
    }

    @Test
    void drawCardsAfterGold() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.takeGold();
                Pair<District, Optional<District>> cards = action.drawCards();
                action.chooseCard(cards.first());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>(List.of(a, b))).build(), cardDrawer);
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, new SelfPlayerView(cardDrawer), null));
    }

    @Test
    void drawCardsInEmptyDeck() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.drawCards();
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), cardDrawer);
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, new SelfPlayerView(cardDrawer), null));
    }

    @Test
    void chooseTwice() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                Pair<District, Optional<District>> cards = action.drawCards();
                action.chooseCard(cards.first());
                action.chooseCard(cards.second().orElseThrow());
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>(List.of(a, b))).build(), cardDrawer);
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, new SelfPlayerView(cardDrawer), null));
    }

    @Test
    void chooseInvalid() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.drawCards();
                action.chooseCard(new District("Amphi forum", Category.MERVEILLE, 9));
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>(List.of(a, b))).build(), cardDrawer);
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, new SelfPlayerView(cardDrawer), null));
    }

    @Test
    void chooseNull() {
        Behavior cardDrawerBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.drawCards();
                action.chooseCard(null);
            }
        };

        Player cardDrawer = createFakePlayer(cardDrawerBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>(List.of(a))).build(), cardDrawer);
        assertThrows(IllegalActionException.class, () -> cardDrawerBehavior.playTurn(action, new SelfPlayerView(cardDrawer), null));
    }

    @Test
    void buildDistrict() throws IllegalActionException {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), districtBuilder);
        districtBuilderBehavior.playTurn(action, new SelfPlayerView(districtBuilder), null);
        assertEquals(a, action.getBuiltDistrict());
    }

    @Test
    void buildDistrictAsArchitect() {
        District c = new District("Another other testing", Category.MERVEILLE, 1);
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
                action.buildDistrict(b);
                action.buildDistrict(c);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        districtBuilder.setCurrentRole(Role.ARCHITECTE);
        districtBuilder.getHand().add(c);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), districtBuilder);

        assertDoesNotThrow(() -> districtBuilderBehavior.playTurn(action, new SelfPlayerView(districtBuilder), null));

        assertTrue(districtBuilder.getCity().getDistricts().contains(a));
        assertTrue(districtBuilder.getCity().getDistricts().contains(b));
        assertTrue(districtBuilder.getCity().getDistricts().contains(c));

        assertEquals(0, ((ArchitecteAbilityAction) action.getAbilityAction()).getRemainingMaxDistricts());
    }

    @Test
    void buildTwoDistricts() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
                action.buildDistrict(b);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), districtBuilder);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, new SelfPlayerView(districtBuilder), null));
    }

    @Test
    void buildInvalidDistrict() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(new District("Gold generator", Category.MERVEILLE, 10));
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), districtBuilder);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, new SelfPlayerView(districtBuilder), null));
    }

    @Test
    void buildDistrictWithoutEnoughGold() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(a);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        districtBuilder.setGold(0);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), districtBuilder);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, new SelfPlayerView(districtBuilder), null));
    }

    @Test
    void buildNullDistrict() {
        Behavior districtBuilderBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                action.buildDistrict(null);
            }
        };

        Player districtBuilder = createRichPlayer(districtBuilderBehavior);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), districtBuilder);
        assertThrows(IllegalActionException.class, () -> districtBuilderBehavior.playTurn(action, new SelfPlayerView(districtBuilder), null));
    }
}