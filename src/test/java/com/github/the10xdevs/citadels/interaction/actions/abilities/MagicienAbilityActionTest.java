package com.github.the10xdevs.citadels.interaction.actions.abilities;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.ExpensiveBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.PlayerView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class MagicienAbilityActionTest {
    @Test
    void useMagicienAbility() {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState, Set<Role> availableRoles) throws IllegalActionException {
                // ...
            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                MagicienAbilityAction magicienAction = (MagicienAbilityAction) action.getAbilityAction();

                List<PlayerView> players = gameState.getPlayers();
                if (!players.isEmpty()) {
                    PlayerView targetPlayer = players.get(0);

                    magicienAction.exchangeHandWith(targetPlayer);
                }

            }
        };

        Game game = new Game(Collections.emptyList());

        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.MAGICIEN);

        RegularTurnAction action = new RegularTurnAction(game, player, new Deck(Collections.emptyList()));

        assertDoesNotThrow(() -> player.getBehavior().playTurn(action, new SelfPlayerView(player), new GameView(game)));

        MagicienAbilityAction magicienAction = (MagicienAbilityAction) action.getAbilityAction();
    }

    @Test
    void testExchangeHandWith() {
        Game game = new Game(List.of(
                new ExpensiveBuilderBehavior(),
                new FastBuilderBehavior()
        ));

        MagicienAbilityAction magicienAction = new MagicienAbilityAction(game.getPlayers().get(0), game);
        PlayerView targetPlayerView = new PlayerView(game.getPlayers().get(1));

        assertDoesNotThrow(() -> magicienAction.exchangeHandWith(targetPlayerView));

        assertEquals(game.getPlayers().get(0).getHand(), game.getPlayers().get(1).getHand());
    }

    @Test
    void testGetExchangedPlayer() throws IllegalActionException {
        Game game = new Game(List.of(
                new ExpensiveBuilderBehavior(),
                new FastBuilderBehavior()
        ));

        MagicienAbilityAction magicienAction = new MagicienAbilityAction(game.getPlayers().get(0), game);
        PlayerView targetPlayerView = new PlayerView(game.getPlayers().get(1));

        assertDoesNotThrow(() -> magicienAction.exchangeHandWith(targetPlayerView));

        PlayerView exchangedPlayer = magicienAction.getExchangedPlayer();

        assertEquals(targetPlayerView, exchangedPlayer);
    }


}

