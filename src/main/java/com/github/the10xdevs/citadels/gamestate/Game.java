package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.logging.ConsoleLogger;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.*;

public class Game {
    private final List<Player> players = new ArrayList<>();
    private final Deck deck = new Deck(District.all());
    private final ConsoleLogger logger = new ConsoleLogger();
    private int firstPlayerIndex = 0;
    private int turn = 1;

    public Game(List<Behavior> behaviors) {
        for (Behavior behavior : behaviors) {
            players.add(new Player(behavior));
        }
    }

    public void start() {
        this.deck.shuffle();
        for (Player player : this.players) {
            for (int i = 0; i < 4; i++) {
                player.getHand().add(this.deck.drawCard());
            }
        }
        try {
            while (!this.isGameOver()) {
                this.logger.logTurnStart(this.turn);
                this.playRoleTurn();
                this.playRegularTurn();
                this.turn++;
            }
            // At the end of the game, sort players by their score (sum of all their district's cost)
            this.players.sort(Comparator.comparingInt((Player player) -> player.getCity().getDistricts().stream()
                    .mapToInt(District::getCost)
                    .sum()).reversed());
            this.logger.logWinners(this.players);
        } catch (IllegalActionException e) {
            this.logger.logError(e);
        }
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getCity().getSize() >= 8) {
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

            this.logger.logRoleTurnAction(i, roleTurnAction);

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
            SelfPlayerView currentPlayerView = new SelfPlayerView(player);
            RegularTurnAction action = new RegularTurnAction(currentPlayerView, this.deck.peekFirstTwo());

            player.getBehavior().playTurn(action, currentPlayerView, new GameView(this));

            this.logger.logRegularTurnAction(player, action);

            if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
                player.incrementGold(2);
            } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
                this.deck.drawCard();
                this.deck.drawCard();
                player.getHand().add(action.getChosenCard());
                this.deck.enqueueCard(action.getDiscardedCard());
            }

            District builtDistrict = action.getBuiltDistrict();
            if (builtDistrict != null) {
                player.incrementGold(-builtDistrict.getCost());
                player.getHand().remove(builtDistrict);
                try {
                    player.getCity().addDistrict(builtDistrict);
                } catch (DuplicatedDistrictException e) {
                    throw new IllegalActionException("Cannot build the same district twice", e);
                }
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