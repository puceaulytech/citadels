package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.GameView;
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
import java.util.EnumSet;
import java.util.List;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class FastBuilderBehaviorTest {
    @Mock GameView state = mock(GameView.class);
    FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
    PlayerView view = new PlayerView(new Player(fastBuilderBehavior));

    private static RegularTurnAction getRegularTurnAction(FastBuilderBehavior fastBuilderBehavior) throws IllegalActionException {
        Game game = new Game(List.of(fastBuilderBehavior));
        Player player = new Player(fastBuilderBehavior);
        RegularTurnAction regularTurnAction = new RegularTurnAction(game, player, new Deck(List.of(
                new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4), new District("e", Category.MILITAIRE, 5)
        )));
        player.setCurrentRole(Role.ARCHITECTE);
        try {
            regularTurnAction.takeGold();
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

        return regularTurnAction;

    }

    @BeforeEach
    void initPlayer() {
        Behavior fastBuilderBehavior = new FastBuilderBehavior();
        Player p = new Player(fastBuilderBehavior);
        p.setCurrentRole(Role.ARCHITECTE);
        p.swapHandWith(new Player(new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {

            }
        }));

        Game game = new Game(List.of(fastBuilderBehavior));
        RegularTurnAction action = new RegularTurnAction(game, p, new Deck(List.of(
                new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4), new District("e", Category.MILITAIRE, 5)
        )));


    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        when(state.getPlayers()).thenReturn(List.of(view, view));
        assertDoesNotThrow(() -> fastBuilderBehavior.pickRole(roleTurnAction, null, state, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest_FastBuilder(Set<Role> availableRoles) {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        assertDoesNotThrow(() -> fastBuilderBehavior.pickRole(roleTurnAction, null, null, availableRoles));
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void playTurnTest_DrawCardUntilEight() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Player pl = new Player(fastBuilderBehavior);
        pl.setGold(8);
        pl.setCurrentRole(Role.MAGICIEN);

        SelfPlayerView selfPlayerView = new SelfPlayerView(pl);
        Game game = new Game(List.of(fastBuilderBehavior));
        GameView gameView = new GameView(game);
        RegularTurnAction regularTurnAction = new RegularTurnAction(game, pl, new Deck(List.of(
                new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4),
                new District("e", Category.MILITAIRE, 5)
        )));

        assertDoesNotThrow(() -> fastBuilderBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
        assertNotNull(regularTurnAction.getChosenCard());
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void playTurnTest_TakeGoldAfterEight() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Player player = new Player(fastBuilderBehavior);
        player.setGold(8);
        player.setCurrentRole(Role.ASSASSIN);
        Game game = new Game(List.of(fastBuilderBehavior));


        RegularTurnAction regularTurnAction = new RegularTurnAction(game, player, new Deck(List.of(
                new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4),
                new District("e", Category.MILITAIRE, 5)
        )));
        SelfPlayerView selfPlayerView = new SelfPlayerView(game.getPlayers().get(0));
        GameView gameView = new GameView(game);

        assertDoesNotThrow(() -> fastBuilderBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));

    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void playTurnTest_BuildAffordableDistrict() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Player player = new Player(fastBuilderBehavior);
        player.setCurrentRole(Role.ARCHITECTE);
        player.setGold(8);
        Game game = new Game(List.of(fastBuilderBehavior));

        RegularTurnAction regularTurnAction = new RegularTurnAction(new Game(List.of(fastBuilderBehavior)),
                player, new Deck(List.of(
                new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4),
                new District("e", Category.MILITAIRE, 5)
        )));
        SelfPlayerView selfPlayerView = new SelfPlayerView(player);
        GameView gameView = new GameView(game);


        assertDoesNotThrow(() -> fastBuilderBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
        assertNotNull(regularTurnAction.getBuiltDistrict());
    }
    @Test
    void pickRoleTest() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        RoleTurnAction roleTurnAction = new RoleTurnAction(EnumSet.allOf(Role.class));
        SelfPlayerView selfPlayerView = new SelfPlayerView(new Player(fastBuilderBehavior));
        GameView gameView = new GameView(new Game(List.of(fastBuilderBehavior)));

        assertDoesNotThrow(() -> fastBuilderBehavior.pickRole(roleTurnAction, selfPlayerView, gameView, EnumSet.allOf(Role.class)));
        assertNotNull(roleTurnAction.getPickedRole());
        assertNotNull(roleTurnAction.getDiscardedRole());
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

}
