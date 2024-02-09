package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class VoleurAbilityActionTest {

    @Test
    void use() throws IllegalActionException {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();
                voleurAction.stealFrom(Role.MAGICIEN);
            }
        };

        Game game = GameBuilder.create().withDeck(new Deck<>()).build();

        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.VOLEUR);

        RegularTurnAction action = new RegularTurnAction(game, player);
        player.getBehavior().playTurn(action, new SelfPlayerView(player), new GameView(game));

        VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();

        assertEquals(Role.MAGICIEN, voleurAction.getStolenRole());
    }

    @Test
    void stealFromAssassin() {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();
                voleurAction.stealFrom(Role.ASSASSIN);
            }
        };

        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.VOLEUR);

        Game game = GameBuilder.create().withDeck(new Deck<>()).build();
        RegularTurnAction action = new RegularTurnAction(game, player);
        assertThrows(IllegalActionException.class, () -> player.getBehavior().playTurn(action, new SelfPlayerView(player), null));
    }


    @Test
    void stealFromStoresRoleAndSetsStolenRoleInGame() throws IllegalActionException {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Game game = GameBuilder.create().addBehavior(fastBuilderBehavior).build();

        VoleurAbilityAction action = new VoleurAbilityAction(game);
        action.stealFrom(Role.EVEQUE);
        assertEquals(Role.EVEQUE, action.getStolenRole());
        assertTrue(game.getStolenRole().isPresent());
        assertEquals(Role.EVEQUE, game.getStolenRole().get());
    }

    @Test
    void stealFromThrowsExceptionWhenRoleIsNull() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Game game = GameBuilder.create().addBehavior(fastBuilderBehavior).build();

        VoleurAbilityAction action = new VoleurAbilityAction(game);
        assertThrows(IllegalActionException.class, () -> action.stealFrom(null));
    }

    @Test
    void stealFromThrowsExceptionWhenStealingFromAssassin() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Game game = GameBuilder.create().addBehavior(fastBuilderBehavior).build();

        VoleurAbilityAction action = new VoleurAbilityAction(game);
        assertThrows(IllegalActionException.class, () -> action.stealFrom(Role.ASSASSIN));
    }

    @Test
    void stealFromThrowsExceptionWhenStealingFromKilledRole() {
        FastBuilderBehavior fastBuilderBehavior = new FastBuilderBehavior();
        Game game = GameBuilder.create().addBehavior(fastBuilderBehavior).build();

        game.setKilledRole(Role.EVEQUE);
        VoleurAbilityAction action = new VoleurAbilityAction(game);
        assertThrows(IllegalActionException.class, () -> action.stealFrom(Role.EVEQUE));
    }
}