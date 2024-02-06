package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.CondottiereAbilityAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class RichardBehaviorTest {
    RichardBehavior richardBehavior = new RichardBehavior();
    Behavior emptyBehavior = new Behavior() {
        @Override
        public void pickRole(RoleTurnAction action, SelfPlayerView self, GameView gameState) {
        }

        @Override
        public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {
        }
    };

    @Test
    void killCondittiereIfAssassinAndLeading() throws DuplicatedDistrictException, IllegalActionException {
        Game game = GameBuilder.create()
                .addBehavior(emptyBehavior)
                .addBehavior(richardBehavior)
                .build();

        Player richard = game.getPlayers().get(1);

        richard.getCity().addDistrict(new District("Stuff", Category.RELIGIEUX, 20));
        richard.setCurrentRole(Role.ASSASSIN);

        RegularTurnAction regularTurnAction = new RegularTurnAction(game, richard);
        richardBehavior.playTurn(regularTurnAction, new SelfPlayerView(richard), new GameView(game));

        AssassinAbilityAction abilityAction = (AssassinAbilityAction) regularTurnAction.getAbilityAction();
        assertEquals(Role.CONDOTTIERE, abilityAction.getKilledRole());
    }
}