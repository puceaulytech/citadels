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
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class AssassinAbilityActionTest {
    @Test
    void use() throws IllegalActionException {
        Behavior testBehavior = new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
                AssassinAbilityAction assassinAction = (AssassinAbilityAction) action.getAbilityAction();
                assassinAction.kill(Role.MAGICIEN);
            }
        };

        Game game = GameBuilder.create().withDeck(new Deck<>()).build();

        Player player = new Player(testBehavior);
        player.setCurrentRole(Role.ASSASSIN);

        RegularTurnAction action = new RegularTurnAction(game, player);
        player.getBehavior().playTurn(action, new SelfPlayerView(player), new GameView(game));

        AssassinAbilityAction assassinAction = (AssassinAbilityAction) action.getAbilityAction();

        assertEquals(Role.MAGICIEN, assassinAction.getKilledRole());
    }
}