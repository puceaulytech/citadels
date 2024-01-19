package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RandomBehaviorTest {
    @BeforeEach
    void initPlayer() {
        Behavior dummyBehavior= new RandomBehavior();
        Player p = new Player(dummyBehavior);
        SelfPlayerView selfPlayerViewTest = new SelfPlayerView(p);
        p.setCurrentRole(Role.ARCHITECTE);
        Game game = new Game(List.of(dummyBehavior));
        RegularTurnAction action = new RegularTurnAction(game, p, new Deck(List.of(
                new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4),new District("e", Category.MILITAIRE, 5)
                )));

    }
    @Mock GameView state = mock(GameView.class);
    RandomBehavior dummyBehavior = new RandomBehavior();
    PlayerView view = new PlayerView(new Player(dummyBehavior));

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        when(state.getPlayers()).thenReturn(List.of(view, view));
        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, null, state, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }
    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest_RandomRole(Set<Role> availableRoles) {
        RandomBehavior dummyBehavior = new RandomBehavior();
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));


        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, null, null, availableRoles));
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void playTurnTest_RandomAction(Set<Role> availableRoles) {
        RandomBehavior dummyBehavior = new RandomBehavior();
        Player dummyPlayer = new Player(dummyBehavior);
        dummyPlayer.setCurrentRole(Role.ARCHITECTE);

        RegularTurnAction regularTurnAction = new RegularTurnAction(new Game(List.of(dummyBehavior)),dummyPlayer , new Deck(List.of()));
        SelfPlayerView selfPlayerView = new SelfPlayerView(dummyPlayer);
        GameView gameView = new GameView(new Game(List.of(dummyBehavior)));

        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void playTurnTest_AssassinAbility(Set<Role> availableRoles) {

        RandomBehavior dummyBehavior = new RandomBehavior();
        Player dummyPlayer = new Player(dummyBehavior);
        dummyPlayer.setCurrentRole(Role.ASSASSIN);

        RegularTurnAction regularTurnAction = new RegularTurnAction(new Game(List.of(dummyBehavior)),dummyPlayer ,
                new Deck(Collections.unmodifiableCollection(List.of(new District("nobleDistrict", Category.NOBLE, 6),
                new District("ReligiousDistrict", Category.RELIGIEUX, 8),
                new District("a", Category.NOBLE, 1),
                new District("b", Category.RELIGIEUX, 2),
                new District("c", Category.MARCHAND, 3),
                new District("d", Category.MERVEILLE, 4),

                new District("e", Category.MILITAIRE, 5),
                new District("f", Category.NOBLE, 6),
                new District("g", Category.MILITAIRE, 2),
                new District("h", Category.MARCHAND, 7),
                new District("i", Category.NOBLE, 9),
                new District("j", Category.RELIGIEUX, 8),
                new District("k", Category.NOBLE, 10),
                new District("l", Category.RELIGIEUX, 11)))));

        SelfPlayerView selfPlayerView = new SelfPlayerView(dummyPlayer);
        GameView gameView = new GameView(new Game(List.of(dummyBehavior)));

        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
        assertNotNull(regularTurnAction.getAbilityAction());
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void playTurnTest_VoleurAbility(Set<Role> availableRoles) {
        RandomBehavior dummyBehavior = new RandomBehavior();
        Player dummyPlayer = new Player(dummyBehavior);
        dummyPlayer.setCurrentRole(Role.VOLEUR);
        RegularTurnAction regularTurnAction = new RegularTurnAction(new Game(List.of(dummyBehavior)),dummyPlayer, new Deck(List.of()));
        SelfPlayerView selfPlayerView = new SelfPlayerView(dummyPlayer);
        GameView gameView = new GameView(new Game(List.of(dummyBehavior)));

        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
        assertNotNull(regularTurnAction.getAbilityAction());
    }



}
