package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
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

class RandomBehaviorTest {
    @Mock
    GameView state = mock(GameView.class);
    RandomBehavior dummyBehavior = new RandomBehavior();
    PlayerView view = new PlayerView(new Player(dummyBehavior));


    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        when(state.getPlayers()).thenReturn(List.of(view, view));
        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, null, state));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }


    @Test
    void playTurnTest_RandomAction() {
        RandomBehavior dummyBehavior = new RandomBehavior();
        Player dummyPlayer = new Player(dummyBehavior);
        dummyPlayer.setCurrentRole(Role.ARCHITECTE);

        RegularTurnAction regularTurnAction = new RegularTurnAction(GameBuilder.create().addBehavior(dummyBehavior).build(), dummyPlayer);
        SelfPlayerView selfPlayerView = new SelfPlayerView(dummyPlayer);
        GameView gameView = new GameView(GameBuilder.create().addBehavior(dummyBehavior).build());

        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
    }

    @Test
    void playTurnTest_AssassinAbility() {

        RandomBehavior dummyBehavior = new RandomBehavior();
        Player dummyPlayer = new Player(dummyBehavior);
        dummyPlayer.setCurrentRole(Role.ASSASSIN);

        RegularTurnAction regularTurnAction = new RegularTurnAction(GameBuilder.create().addBehavior(dummyBehavior).withDeck(
                new Deck<>(List.of(new District("nobleDistrict", Category.NOBLE, 6),
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
                        new District("l", Category.RELIGIEUX, 11)))).build(), dummyPlayer);

        SelfPlayerView selfPlayerView = new SelfPlayerView(dummyPlayer);
        GameView gameView = new GameView(GameBuilder.create().addBehavior(dummyBehavior).build());

        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
        assertNotNull(regularTurnAction.getAbilityAction());
    }

    @Test
    void playTurnTest_VoleurAbility() {
        RandomBehavior dummyBehavior = new RandomBehavior();
        Player dummyPlayer = new Player(dummyBehavior);
        dummyPlayer.setCurrentRole(Role.VOLEUR);
        RegularTurnAction regularTurnAction = new RegularTurnAction(GameBuilder.create().addBehavior(dummyBehavior).build(), dummyPlayer);
        SelfPlayerView selfPlayerView = new SelfPlayerView(dummyPlayer);
        GameView gameView = new GameView(GameBuilder.create().addBehavior(dummyBehavior).build());

        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, selfPlayerView, gameView));
        assertNotNull(regularTurnAction.getAbilityAction());
    }

    @Test
    void use_AssassinKillsMagician_ShouldSetKilledRoleToMagician() throws IllegalActionException {
        // Arrange
        Behavior testBehavior = new TestBehaviorForAssassin(Role.MAGICIEN);

        Game game = GameBuilder.create().build();
        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.ASSASSIN);
        RegularTurnAction action = new RegularTurnAction(game, player);

        // Act
        player.getBehavior().playTurn(action, new SelfPlayerView(player), new GameView(game));
        AssassinAbilityAction assassinAction = (AssassinAbilityAction) action.getAbilityAction();

        // Assert
        assertEquals(Role.MAGICIEN, assassinAction.getKilledRole());
    }

    private static class TestBehaviorForAssassin implements Behavior {
        private final Role roleToKill;

        public TestBehaviorForAssassin(Role roleToKill) {
            this.roleToKill = roleToKill;
        }

        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {

        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
            AssassinAbilityAction assassinAction = (AssassinAbilityAction) action.getAbilityAction();
            assassinAction.kill(roleToKill);
        }
    }
}
