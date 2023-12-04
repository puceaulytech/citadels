package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import com.github.the10xdevs.citadels.models.Role;

import java.util.*;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private int firstPlayerIndex = 0;
    private final Deck deck = new Deck();

    public Game(List<Behavior> behaviors) {
        for (Behavior behavior : behaviors) {
            players.add(new Player(behavior));
        }
    }

    public void start() {
        try {
            this.playRoleTurn();
        } catch (IllegalActionException e) {
            System.out.println("Something went wrong during play: " + e.getMessage());
        }
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

    public List<Player> getPlayers() {
        return players;
    }

    public Deck getDeck() {
        return deck;
    }
}
