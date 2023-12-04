package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.*;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private final Deck deck = new Deck(District.all);
    private int firstPlayerIndex = 0;
    private int turn = 1;

    public Game(List<Behavior> behaviors) {
        for (Behavior behavior : behaviors) {
            players.add(new Player(behavior));
        }
    }

    public void start() {
        try {
            while (!this.isGameOver()) {
                this.playRoleTurn();
                this.playRegularTurn();
                turn++;
            }
            System.out.println("Le jeu est terminé!");
        } catch (IllegalActionException e) {
            System.out.println("Something went wrong during play: " + e.getMessage());
        }
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getCity().getSize() >= 8) {
                System.out.println("Un joueur a construit suffisamment de districts.");
                return true;
            }
        }
        return false;
    }


    private void playRoleTurn() throws IllegalActionException {
        // Create a set with all available roles
        Set<Role> roles = EnumSet.allOf(Role.class);
        for (int i = 0; i < this.players.size(); i++) {
            // Get next player to play
            Player player = this.players.get((i + firstPlayerIndex) % this.players.size());

            RoleTurnAction roleTurnAction = new RoleTurnAction();
            player.getBehavior().pickRole(roleTurnAction, Collections.unmodifiableSet(roles));

            Role pickedRole = roleTurnAction.getPickedRole();
            Role discardedRole = roleTurnAction.getDiscardedRole();

            if (!roles.contains(pickedRole) || !roles.contains(discardedRole) || pickedRole == discardedRole) {
                throw new IllegalActionException("Picked role and discarded role are the same");
            }

            player.setCurrentRole(pickedRole);
            roles.remove(pickedRole);
            roles.remove(discardedRole);
        }
    }

    private void playRegularTurn() throws IllegalActionException {
        // Sort players according to their role
        this.players.sort(Comparator.comparingInt(player -> player.getCurrentRole().getTurnOrder()));

        for (Player player : this.players) {
            RegularTurnAction action = new RegularTurnAction(player.getCurrentRole().getAbilityAction(), this.deck.peekFirstTwo());

            player.getBehavior().playTurn(action, new SelfPlayerView(player), new GameView(this));

            if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
                player.incrementGold(2);
            } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
                this.deck.drawCard();
                this.deck.drawCard();
                player.getHand().add(action.getChosenCard());
                this.deck.enqueueCard(action.getDiscardedCard());
            }
        }
    }

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }

    public int getTurn() {
        return turn;
    }

   
}