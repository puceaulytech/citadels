package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.GameBuilder;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.FastBuilderBehavior;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class GameViewTest {
    @Test
    void cannotAddPlayer() {
        GameView gameView = new GameView(GameBuilder.create().build());
        Player player = new Player(new FastBuilderBehavior());
        assertThrows(Exception.class, () -> gameView.getPlayers().add(new PlayerView(player)));
    }

    @Test
    void getDeckSizeException() {
        Game game = GameBuilder.create().addBehavior(new FastBuilderBehavior()).build();
        GameView gameView = new GameView(game);
        assertEquals(65, gameView.getDeckSize());


        Player player = new Player(new FastBuilderBehavior());
        player.setCurrentRole(Role.ARCHITECTE);

        Deck deck = new Deck();
        deck.enqueueCard(Role.ROI);
        deck.enqueueCard(Role.MAGICIEN);
        deck.enqueueCard(Role.VOLEUR);
        deck.enqueueCard(Role.EVEQUE);
        deck.enqueueCard(Role.CONDOTTIERE);


        FastBuilderBehavior behavior = new FastBuilderBehavior();
        RegularTurnAction action = new RegularTurnAction(game, player, deck);

        try {

            action.drawCards();
        } catch (IllegalActionException e) {
            throw new RuntimeException(e);
        }

    }

    @Test
    void getRolesFacingUpException() {
        GameView gameView = new GameView(GameBuilder.create().build());
        assertEquals(0, gameView.getRolesFacingUp().size());
    }
}