package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class MagicienAbilityActionTest {
    Behavior emptyBehavior = new Behavior() {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
        }
    };

    District card0 = new District("Baraque de Chardinoob", Category.MILITAIRE, 2);
    District card1 = new District("Baraque de Logan", Category.MERVEILLE, 2);
    District card2 = new District("Baraque de Vahan", Category.MILITAIRE, 6);
    District card3 = new District("Baraque de Robin", Category.MERVEILLE, 5);

    Game game = GameBuilder.create()
            .addBehavior(emptyBehavior)
            .addBehavior(emptyBehavior)
            .withDeck(new Deck<>(List.of(card2, card3)))
            .build();

    Player target = game.getPlayers().get(0);

    @BeforeEach
    void setupFakePlayer() {
        target.setCurrentRole(Role.ROI);
        target.getHand().add(card0);
        target.getHand().add(card1);
    }

    @Test
    void testExchangeHandWith() {
        Behavior swapper = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                MagicienAbilityAction abilityAction = (MagicienAbilityAction) action.getAbilityAction();
                abilityAction.exchangeHandWith(gameState.getPlayers().get(0));
            }
        };

        Player swapperPlayer = new Player(swapper);
        swapperPlayer.setCurrentRole(Role.MAGICIEN);
        RegularTurnAction action = new RegularTurnAction(game, swapperPlayer);

        assertDoesNotThrow(() -> swapper.playTurn(action, new SelfPlayerView(swapperPlayer), new GameView(game)));
        MagicienAbilityAction abilityAction = (MagicienAbilityAction) action.getAbilityAction();
        assertTrue(abilityAction.getExchangedPlayer().represents(target));
        assertEquals(card0, swapperPlayer.getHand().get(0));
        assertEquals(card1, swapperPlayer.getHand().get(1));
    }

    @Test
    void testExchangeWithNonExistingPlayer() {
        Behavior swapper = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                MagicienAbilityAction abilityAction = (MagicienAbilityAction) action.getAbilityAction();
                abilityAction.exchangeHandWith(new PlayerView(new Player(emptyBehavior)));
            }
        };

        Player swapperPlayer = new Player(swapper);
        swapperPlayer.setCurrentRole(Role.MAGICIEN);
        RegularTurnAction action = new RegularTurnAction(game, swapperPlayer);

        assertThrows(IllegalActionException.class, () -> swapper.playTurn(action, new SelfPlayerView(swapperPlayer), new GameView(game)));
    }

    @Test
    void testDiscardAndDraw() {
        Behavior discarder = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                MagicienAbilityAction abilityAction = (MagicienAbilityAction) action.getAbilityAction();
                abilityAction.discardAndDraw(new ArrayList<>(self.getHand()));
            }
        };

        Player discarderPlayer = new Player(discarder);
        discarderPlayer.setCurrentRole(Role.MAGICIEN);
        discarderPlayer.getHand().add(card0);
        discarderPlayer.getHand().add(card1);
        RegularTurnAction action = new RegularTurnAction(game, discarderPlayer);

        assertDoesNotThrow(() -> discarder.playTurn(action, new SelfPlayerView(discarderPlayer), new GameView(game)));
        assertTrue(discarderPlayer.getHand().contains(card2));
        assertTrue(discarderPlayer.getHand().contains(card3));
    }

    @Test
    void testIllegalDiscardAndDraw() {
        Behavior discarder = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                MagicienAbilityAction abilityAction = (MagicienAbilityAction) action.getAbilityAction();
                abilityAction.discardAndDraw(List.of(card0));
            }
        };

        Player discarderPlayer = new Player(discarder);
        discarderPlayer.setCurrentRole(Role.MAGICIEN);

        RegularTurnAction action = new RegularTurnAction(game, discarderPlayer);
        assertThrows(IllegalActionException.class, () -> discarder.playTurn(action, new SelfPlayerView(discarderPlayer), new GameView(game)));
    }
}