package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
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
        testView = new PlayerView(testPlayer);
        gameBuilder = GameBuilder.create();

        when(state.getTurn()).thenReturn(1);
        when(state.getPlayers()).thenReturn(List.of(testView, testView));
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        assertDoesNotThrow(() -> behavior.pickRole(roleTurnAction, selfTestPlayer, state, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @ParameterizedTest
    @EnumSource(Role.class)
    void playTurnTest(Role role) {
        Deck<District> deck = new Deck<>(List.of(house, yacht));
        testPlayer.setCurrentRole(role);
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(deck).build(), testPlayer);
        assertDoesNotThrow(() -> behavior.playTurn(action, selfTestPlayer, state));
    }

    @Test
    void pickInterestingRoleLateGame() throws DuplicatedDistrictException, IllegalActionException {
        when(state.getTurn()).thenReturn(20);

        testPlayer.getCity().addDistrict(house);
        testPlayer.getCity().addDistrict(yacht);
        testPlayer.getCity().addDistrict(car);

        Set<Role> roles = EnumSet.of(Role.ASSASSIN, Role.CONDOTTIERE, Role.ROI);
        RoleTurnAction action = new RoleTurnAction(roles);
        behavior.pickRole(action, selfTestPlayer, state, roles);
        assertEquals(Role.CONDOTTIERE, action.getPickedRole());
    }

    @Test
    void pickCardToHaveOneOfEachColor() throws DuplicatedDistrictException, IllegalActionException {
        testPlayer.getCity().addDistrict(house);
        testPlayer.getCity().addDistrict(car);
        testPlayer.getCity().addDistrict(plane);
        testPlayer.getCity().addDistrict(glasses);

        testPlayer.setCurrentRole(Role.ASSASSIN);
        testPlayer.setGold(10);
        Deck<District> deck = new Deck<>(List.of(shirt, yacht));
        RegularTurnAction action = new RegularTurnAction(gameBuilder.withDeck(deck).build(), testPlayer);
        behavior.playTurn(action, selfTestPlayer, state);

        assertEquals(shirt, action.getChosenCard());
    }
}