package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FastBuilderBehaviorTest {
    Game game;
    @Mock
    GameView state = mock(GameView.class);
    FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
    Player player;
    PlayerView view;

    @BeforeEach
    void initPlayer() {
        Behavior fastBuilderBehavior = new FastBuilderBehavior();
        player = new Player(fastBuilderBehavior);
        view = new PlayerView(player);

        game = GameBuilder.create().addBehavior(fastBuilderBehavior).build();
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        when(state.getPlayers()).thenReturn(List.of(view, view));
        assertDoesNotThrow(() -> fastBuilderBehavior.pickRole(roleTurnAction, new SelfPlayerView(player), state));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @Test
    void playTurnTest_DrawCardUntilEight() {
        player.setGold(8);
        player.setCurrentRole(Role.MAGICIEN);

        SelfPlayerView selfPlayerView = new SelfPlayerView(player);
        GameView gameView = new GameView(game);

        RegularTurnAction action = new RegularTurnAction(game, player);
        assertDoesNotThrow(() -> fastBuilderBehavior.playTurn(action, selfPlayerView, gameView));
        assertNotNull(action.getChosenCard());
    }

    @Test
    void playTurnTest_TakeGoldAfterEight() {
        player.setGold(8);
        player.setCurrentRole(Role.ASSASSIN);

        SelfPlayerView selfPlayerView = new SelfPlayerView(player);
        GameView gameView = new GameView(game);

        RegularTurnAction action = new RegularTurnAction(game, player);
        assertDoesNotThrow(() -> fastBuilderBehavior.playTurn(action, selfPlayerView, gameView));
    }

    @Test
    void playTurnTest_BuildAffordableDistrict() {
        player.setCurrentRole(Role.ARCHITECTE);
        player.setGold(8);

        SelfPlayerView selfPlayerView = new SelfPlayerView(player);
        GameView gameView = new GameView(game);

        RegularTurnAction action = new RegularTurnAction(game, player);
        assertDoesNotThrow(() -> fastBuilderBehavior.playTurn(action, selfPlayerView, gameView));
        assertNotNull(action.getBuiltDistrict());
    }

    @Test
    void pickRoleTest() {
        RoleTurnAction roleTurnAction = new RoleTurnAction(EnumSet.allOf(Role.class));
        SelfPlayerView selfPlayerView = new SelfPlayerView(player);

        GameView gameView = new GameView(game);

        try {
            roleTurnAction.discard(Role.MAGICIEN);
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        assertDoesNotThrow(() -> fastBuilderBehavior.pickRole(roleTurnAction, selfPlayerView, gameView));
        assertNotNull(roleTurnAction.getPickedRole());
        assertNotNull(roleTurnAction.getDiscardedRole());
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }
}