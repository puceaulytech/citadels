package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class CondottiereAbilityActionTest {
    Behavior emptyBehavior = new Behavior() {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) {
        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
        }
    };

    District targetDistrict = new District("Baraque à Logan", Category.MERVEILLE, 7);

    Behavior testBehavior = new Behavior() {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) {

        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
            CondottiereAbilityAction abilityAction = (CondottiereAbilityAction) action.getAbilityAction();
            abilityAction.destroy(gameState.getPlayers().get(1), targetDistrict);
        }
    };

    Game game = new Game(List.of(emptyBehavior, testBehavior));

    @Test
    void use() throws IllegalActionException, DuplicatedDistrictException {
        // Setup player
        Player currentPlayer = game.getPlayers().get(0);
        currentPlayer.setGold(10);
        currentPlayer.setCurrentRole(Role.CONDOTTIERE);

        // Setup opponent
        Player opponent = game.getPlayers().get(1);
        opponent.setCurrentRole(Role.ROI);
        opponent.getCity().addDistrict(targetDistrict);

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, currentPlayer, new Deck<>());

        testBehavior.playTurn(regularTurnAction, new SelfPlayerView(currentPlayer), new GameView(game));

        // Check that actions were made
        CondottiereAbilityAction condottiereAbilityAction = (CondottiereAbilityAction) regularTurnAction.getAbilityAction();

        assertEquals(targetDistrict, condottiereAbilityAction.getTargetDistrict());
        assertTrue(condottiereAbilityAction.getTargetPlayer().represents(opponent));
    }

    @Test
    void destroyEveque() throws DuplicatedDistrictException {
        // Setup player
        Player currentPlayer = game.getPlayers().get(0);
        currentPlayer.setGold(10);
        currentPlayer.setCurrentRole(Role.CONDOTTIERE);

        // Setup opponent
        Player opponent = game.getPlayers().get(1);
        opponent.setCurrentRole(Role.EVEQUE);
        opponent.getCity().addDistrict(targetDistrict);

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, currentPlayer, new Deck<>());

        assertThrows(IllegalActionException.class, () -> testBehavior.playTurn(regularTurnAction, new SelfPlayerView(currentPlayer), new GameView(game)));
    }

    @Test
    void destroyButDontHaveGold() throws DuplicatedDistrictException {
        // Setup player
        Player currentPlayer = game.getPlayers().get(0);
        currentPlayer.setGold(0);
        currentPlayer.setCurrentRole(Role.CONDOTTIERE);

        // Setup opponent
        Player opponent = game.getPlayers().get(1);
        opponent.setCurrentRole(Role.ROI);
        opponent.getCity().addDistrict(targetDistrict);

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, currentPlayer, new Deck<>());

        assertThrows(IllegalActionException.class, () -> testBehavior.playTurn(regularTurnAction, new SelfPlayerView(currentPlayer), new GameView(game)));
    }

    @Test
    void destroyFinishedCity() throws DuplicatedDistrictException {
        // Setup player
        Player currentPlayer = game.getPlayers().get(0);
        currentPlayer.setGold(20);
        currentPlayer.setCurrentRole(Role.CONDOTTIERE);

        // Setup opponent
        Player opponent = game.getPlayers().get(1);
        opponent.setCurrentRole(Role.ROI);
        opponent.getCity().addDistrict(targetDistrict);
        for (int i = 0; i < 7; i++) {
            opponent.getCity().addDistrict(new District("Fake n" + i, Category.RELIGIEUX, 2));
        }

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, currentPlayer, new Deck<>());

        assertThrows(IllegalActionException.class, () -> testBehavior.playTurn(regularTurnAction, new SelfPlayerView(currentPlayer), new GameView(game)));
    }

    @Test
    void destroyInvalidDistrict() {
        // Setup player
        Player currentPlayer = game.getPlayers().get(0);
        currentPlayer.setGold(20);
        currentPlayer.setCurrentRole(Role.CONDOTTIERE);

        // Setup opponent
        Player opponent = game.getPlayers().get(1);
        opponent.setCurrentRole(Role.ROI);

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, currentPlayer, new Deck<>());

        assertThrows(IllegalActionException.class, () -> testBehavior.playTurn(regularTurnAction, new SelfPlayerView(currentPlayer), new GameView(game)));
    }

    @Test
    void destroyInvalidPlayer() {
        Behavior badBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                CondottiereAbilityAction abilityAction = (CondottiereAbilityAction) action.getAbilityAction();
                abilityAction.destroy(new PlayerView(new Player(emptyBehavior)), targetDistrict);
            }
        };

        // Setup player
        Player currentPlayer = game.getPlayers().get(0);
        currentPlayer.setGold(20);
        currentPlayer.setCurrentRole(Role.CONDOTTIERE);

        // Setup opponent
        Player opponent = game.getPlayers().get(1);
        opponent.setCurrentRole(Role.ROI);

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, currentPlayer, new Deck<>());

        assertThrows(IllegalActionException.class, () -> badBehavior.playTurn(regularTurnAction, new SelfPlayerView(currentPlayer), new GameView(game)));
    }
}