package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
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
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ExpensiveBuilderBehaviorTest {
    ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
    GameBuilder gameBuilder;
    @Mock
    GameView state = mock(GameView.class);

    Player testPlayer;
    SelfPlayerView selfTestPlayer;
    PlayerView testView;

    @BeforeEach
    void initPlayer() {
        testPlayer = new Player(behavior);
        selfTestPlayer = new SelfPlayerView(testPlayer);
        testView = new PlayerView(testPlayer);
        testPlayer.setCurrentRole(Role.ARCHITECTE);

        gameBuilder = GameBuilder.create();

        when(state.getPlayers()).thenReturn(List.of(testView, testView));
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        assertDoesNotThrow(() -> behavior.pickRole(roleTurnAction, null, state));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @Test
    void testPlayTurn() {
        Deck<District> deck = new Deck<>(List.of(new District("nobleDistrict", Category.NOBLE, 6), new District("ReligiousDistrict", Category.RELIGIEUX, 8)));
        Game game = gameBuilder.withDeck(deck).build();
        RegularTurnAction action = new RegularTurnAction(game, testPlayer);
        assertDoesNotThrow(() -> behavior.playTurn(action, selfTestPlayer, new GameView(game)));
    }

    @Test
    void testPlayTurnMarkingDistrict() throws IllegalActionException {
        Deck<District> deck = new Deck<>();

        District notGoodEnough = new District("DaHood", Category.MARCHAND, 2);
        District goodButNotBest = new District("NobleDistrict", Category.NOBLE, 7);
        District best = new District("ReligiousDistrict", Category.RELIGIEUX, 8);
        testPlayer.getHand().addAll(List.of(notGoodEnough, goodButNotBest, best));
        testPlayer.incrementGold(10);
        Game game = gameBuilder.withDeck(deck).build();

        // he should mark a district for the next turn
        RegularTurnAction action = new RegularTurnAction(game, testPlayer);
        behavior.playTurn(action, selfTestPlayer, new GameView(game));
        assertEquals(RegularTurnAction.BasicAction.GOLD, action.getBasicAction());
        assertNull(action.getBuiltDistrict());

        // he should build the district with the highest score
        action = new RegularTurnAction(game, testPlayer);
        behavior.playTurn(action, selfTestPlayer, new GameView(game));
        assertEquals(best, action.getBuiltDistrict());
    }
}