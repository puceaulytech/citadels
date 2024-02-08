package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.MagicienAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class TryharderBehaviorTest {
    TryharderBehavior behavior = new TryharderBehavior();
    GameBuilder gameBuilder;
    @Mock
    GameView state = mock(GameView.class);

    Player testPlayer;
    SelfPlayerView selfTestPlayer;
    PlayerView testView;

    // Create some districts
    District house = new District("Elon Musk's House", Category.NOBLE, 6);
    District yacht = new District("Elon Musk's Yacht", Category.MILITAIRE, 4);
    District car = new District("Elon Musk's Car", Category.MILITAIRE, 3);
    District plane = new District("Elon Musk's Plane", Category.MARCHAND, 10);
    District glasses = new District("Elon Musk's Glasses", Category.RELIGIEUX, 1);
    District shirt = new District("Elon Musk's Shirt", Category.MERVEILLE, 2);

    @BeforeEach
    void initPlayerAndMock() {
        testPlayer = new Player(behavior);
        selfTestPlayer = new SelfPlayerView(testPlayer);
        testView = new PlayerView(new Player(null));
        gameBuilder = GameBuilder.create();

        // Fake an early game and a game with two players
        when(state.getTurn()).thenReturn(1);
        when(state.getPlayers()).thenReturn(List.of(testView, testView));
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        assertDoesNotThrow(() -> behavior.pickRole(roleTurnAction, selfTestPlayer, state));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void playTurnTest(Role role) {
        // Check if no errors are thrown when playing with every single role
        Deck<District> deck = new Deck<>(List.of(house, yacht));
        testPlayer.setCurrentRole(role);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(deck).build(), testPlayer);
        assertDoesNotThrow(() -> behavior.playTurn(action, selfTestPlayer, state));
    }

    @Test
    void pickInterestingRoleLateGame() throws DuplicatedDistrictException, IllegalActionException {
        // Fake a late game
        when(state.getTurn()).thenReturn(20);

        // Player has one noble district and two military districts
        testPlayer.getCity().addDistrict(house);
        testPlayer.getCity().addDistrict(yacht);
        testPlayer.getCity().addDistrict(car);

        // Test if when given the following roles it chooses the Military Role over the others
        Set<Role> roles = EnumSet.of(Role.ASSASSIN, Role.CONDOTTIERE, Role.ROI);
        RoleTurnAction action = new RoleTurnAction(roles);
        behavior.pickRole(action, selfTestPlayer, state);
        assertEquals(Role.CONDOTTIERE, action.getPickedRole());
    }

    @Test
    void pickCardWhenDeckHasOne() {
        // Check if no errors are thrown when it wants to pick cards but there's only one available
        testPlayer.setCurrentRole(Role.ASSASSIN);
        testPlayer.setGold(10);
        Deck<District> deck = new Deck<>(List.of(yacht));
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(deck).build(), testPlayer);

        assertDoesNotThrow(() -> behavior.playTurn(action, selfTestPlayer, state));
        assertEquals(yacht, action.getChosenCard());
    }

    @Test
    void pickCardByScore() throws IllegalActionException {
        testPlayer.setCurrentRole(Role.ASSASSIN);
        testPlayer.setGold(10);
        // Here the player has no built district so both cards are as "interesting" for it
        // So it should choose the one with the highest score
        Deck<District> deck = new Deck<>(List.of(shirt, yacht));
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(deck).build(), testPlayer);
        behavior.playTurn(action, selfTestPlayer, state);

        assertEquals(yacht, action.getChosenCard());
    }

    @Test
    void pickCardToHaveOneOfEachColor() throws DuplicatedDistrictException, IllegalActionException {
        // The player has built one district of each category except the Special Category
        testPlayer.getCity().addDistrict(house);
        testPlayer.getCity().addDistrict(car);
        testPlayer.getCity().addDistrict(plane);
        testPlayer.getCity().addDistrict(glasses);

        testPlayer.setCurrentRole(Role.ASSASSIN);
        testPlayer.setGold(10);
        // Given one card with the special category and one card with the military category
        Deck<District> deck = new Deck<>(List.of(shirt, yacht));
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(deck).build(), testPlayer);
        behavior.playTurn(action, selfTestPlayer, state);

        // It should choose the card with the special category
        assertEquals(shirt, action.getChosenCard());
    }

    @Test
    void buildDistrictOfDifferentColors() throws DuplicatedDistrictException, IllegalActionException {
        // The player has built one noble, one military and one trade district
        testPlayer.getCity().addDistrict(house);
        testPlayer.getCity().addDistrict(car);
        testPlayer.getCity().addDistrict(plane);
        // It has one special and one military district in hand
        testPlayer.getHand().add(shirt);
        testPlayer.getHand().add(yacht);

        testPlayer.setCurrentRole(Role.ASSASSIN);
        // With enough gold to build both
        testPlayer.setGold(10);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), testPlayer);
        behavior.playTurn(action, selfTestPlayer, state);

        // It should build the special district
        assertEquals(shirt, action.getBuiltDistrict());
    }

    @Test
    void killMerchantByDefault() throws IllegalActionException {
        // When the player has the assassin role it should kill the merchant if possible
        testPlayer.setCurrentRole(Role.ASSASSIN);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(new Deck<>()).build(), testPlayer);
        behavior.playTurn(action, selfTestPlayer, state);

        AssassinAbilityAction ability = (AssassinAbilityAction) action.getAbilityAction();
        assertEquals(Role.MARCHAND, ability.getKilledRole());
    }

    @Test
    void killMagicianWhenMerchantIsFacedUp() throws IllegalActionException {
        // When the player has the assassin role and the merchant is in the cards facing up
        // It should kill the magician
        testPlayer.setCurrentRole(Role.ASSASSIN);
        Game fake = mock(Game.class);
        when(fake.getRolesFacingUp()).thenReturn(EnumSet.of(Role.MARCHAND));

        when(fake.getDeck()).thenReturn(new Deck<>());
        RegularTurnAction action = new RegularTurnAction(fake, testPlayer);
        behavior.playTurn(action, selfTestPlayer, new GameView(fake));

        AssassinAbilityAction ability = (AssassinAbilityAction) action.getAbilityAction();
        assertEquals(Role.MAGICIEN, ability.getKilledRole());
    }

    @Test
    void chooseArchitectWhenRichAndLateGame() throws IllegalActionException {
        // When it is pretty late in the game and the player has a lot of gold and some cards
        // It should pick the architect role
        when(state.getTurn()).thenReturn(20);
        testPlayer.setGold(10);
        for (int i = 0; i < 5; i++)
            testPlayer.getHand().add(new District("Minecraft diamond house", Category.MERVEILLE, i));

        RoleTurnAction roleTurnAction = new RoleTurnAction(EnumSet.allOf(Role.class));
        behavior.pickRole(roleTurnAction, selfTestPlayer, state);

        assertEquals(Role.ARCHITECTE, roleTurnAction.getPickedRole());
    }

    @Test
    void swapCardsWithPlayerThatHasTheMostCards() throws IllegalActionException {
        // In this case, the player has no cards in hand and is facing one player with three cards and another that only has one
        // With the magician role, it should swap hands with the one that has the most cards
        Game game = gameBuilder.withDeck(new Deck<>()).addBehavior(null).addBehavior(null).build();
        Player playerWith3Cards = game.getPlayers().get(0);
        playerWith3Cards.getHand().add(house);
        playerWith3Cards.getHand().add(yacht);
        playerWith3Cards.getHand().add(car);
        Player playerWith1Card = game.getPlayers().get(1);
        playerWith1Card.getHand().add(glasses);

        testPlayer.setCurrentRole(Role.MAGICIEN);
        RegularTurnAction action = new RegularTurnAction(game, testPlayer);
        behavior.playTurn(action, selfTestPlayer, new GameView(game));

        MagicienAbilityAction ability = (MagicienAbilityAction) action.getAbilityAction();
        assertTrue(ability.getExchangedPlayer().represents(playerWith3Cards));
    }

    @Test
    void discardAndDrawWorstCards() throws IllegalActionException, DuplicatedDistrictException {
        // In this case the player has three cards in hand, with a score of 1, 2 and 3 respectively
        // Because no other player has twice as many cards as it, it should try to discard as many cards as there is in the deck
        // And it should try to discard its worst cards
        Game game = gameBuilder.withDeck(new Deck<>(List.of(plane, house))).addBehavior(null).build();

        for (int i = 0; i < 4; i++)
            testPlayer.getCity().addDistrict(new District("Building", Category.MARCHAND, i));
        testPlayer.setCurrentRole(Role.MAGICIEN);
        testPlayer.setGold(-100);
        testPlayer.getHand().add(car);
        testPlayer.getHand().add(shirt);
        testPlayer.getHand().add(glasses);

        RegularTurnAction action = new RegularTurnAction(game, testPlayer);
        behavior.playTurn(action, selfTestPlayer, new GameView(game));

        assertTrue(testPlayer.getHand().contains(car));
        assertFalse(testPlayer.getHand().contains(shirt));
        assertFalse(testPlayer.getHand().contains(glasses));
        assertTrue(testPlayer.getHand().contains(plane));
        assertTrue(testPlayer.getHand().contains(house));
    }
}
