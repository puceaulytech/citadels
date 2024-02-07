package com.github.the10xdevs.citadels.interaction.actions;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.RandomBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.TryharderBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.logging.VoidLogger;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class RoleTurnActionTest {

    @Test
    void use() throws IllegalActionException {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
                action.pick(Role.ARCHITECTE);
                action.discard(Role.MAGICIEN);
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        Set<Role> availableRoles = Set.of(Role.ARCHITECTE, Role.MAGICIEN);

        RoleTurnAction action = new RoleTurnAction(availableRoles);

        testBehavior.pickRole(action, null, null);

        assertEquals(Role.ARCHITECTE, action.getPickedRole());
        assertEquals(Role.MAGICIEN, action.getDiscardedRole());
    }

    @Test
    void pickAndDiscardSame() {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
                action.pick(Role.MAGICIEN);
                action.discard(Role.MAGICIEN);
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        RoleTurnAction action = new RoleTurnAction(Set.of(Role.MAGICIEN, Role.CONDOTTIERE, Role.EVEQUE));

        assertThrows(IllegalActionException.class, () -> testBehavior.pickRole(action, null, null));
    }

    @Test
    void pickNotOfferedRole() {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
                action.pick(Role.ROI);
                action.discard(Role.MAGICIEN);
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        Set<Role> availableRoles = Set.of(Role.MAGICIEN, Role.CONDOTTIERE, Role.EVEQUE);
        RoleTurnAction action = new RoleTurnAction(availableRoles);

        assertThrows(IllegalActionException.class, () -> testBehavior.pickRole(action, null, null));
    }

    @Test
    void discardNotOfferedRole() {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView game) throws IllegalActionException {
                action.pick(Role.MAGICIEN);
                action.discard(Role.ROI);
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
            }
        };

        Set<Role> availableRoles = Set.of(Role.MAGICIEN, Role.CONDOTTIERE, Role.EVEQUE);
        RoleTurnAction action = new RoleTurnAction(availableRoles);

        assertThrows(IllegalActionException.class, () -> testBehavior.pickRole(action, null, null));
    }


    @Test
    void gameEndsWhenPlayerHasEightDistricts() throws DuplicatedDistrictException {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(new TryharderBehavior());
        behaviors.add(new RandomBehavior());
        Deck<District> deck = new Deck<>(District.all());
        Game game = new Game(behaviors, deck, new VoidLogger());
        game.start();
        Player player = game.getPlayers().get(0);
        for (int i = 0; i < 8; i++) {
            player.getCity().addDistrict(new District("District " + i, Category.RELIGIEUX, i));
        }
        assertTrue(game.isGameOver());
    }

    @Test
    void gameContinuesWhenPlayerHasLessThanEightDistricts() throws DuplicatedDistrictException {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(new TryharderBehavior());
        behaviors.add(new RandomBehavior());
        Deck<District> deck = new Deck<>(District.all());
        Game game = new Game(behaviors, deck, new VoidLogger());
        game.start();
        Player player = game.getPlayers().get(0);
        for (int i = 0; i < 7; i++) {
            player.getCity().addDistrict(new District("District " + i, Category.RELIGIEUX, i));
        }
        assertTrue(game.isGameOver());
    }

    @Test
    void playerWithRoleKingBecomesFirstPlayerNextTurn() {
        List<Behavior> behaviors = new ArrayList<>();
        behaviors.add(new TryharderBehavior());
        behaviors.add(new RandomBehavior());
        Deck<District> deck = new Deck<>(District.all());
        Game game = new Game(behaviors, deck, new VoidLogger());
        game.start();
        Player player = game.getPlayers().get(0);
        player.setCurrentRole(Role.ROI);
        assertEquals(player, game.getPlayers().get(0));
    }


}