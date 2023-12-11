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

        // Give four cards to each player
        for (Player player : this.players) {
            for (int i = 0; i < 4; i++) {
                player.getHand().add(this.deck.drawCard());
            }
        }

        try {
            // Main game loop
            while (!this.isGameOver()) {
                this.logger.logTurnStart(this.turn);
                this.playRoleTurn();
                this.playRegularTurn();
                this.determineNextFirstPlayer();
                this.turn++;
            }
        } catch (IllegalActionException e) {
            this.logger.logError(e);
        }

        // At the end of the game, sort players by their score (sum of all their district's cost)
        this.players.sort(Comparator.comparingInt(Player::getScore).reversed());
        this.logger.logWinners(this.players);
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

            RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(roles));

            try {
                player.getBehavior().pickRole(roleTurnAction, new SelfPlayerView(player), new GameView(this), Collections.unmodifiableSet(roles));
            } catch (Exception e) {
                throw new IllegalActionException("Player failed to pick role", e);
            }

            this.logger.logRoleTurnAction(i, player, roleTurnAction);

            player.setCurrentRole(roleTurnAction.getPickedRole());
            roles.remove(roleTurnAction.getPickedRole());
            roles.remove(roleTurnAction.getDiscardedRole());
        }
    }

    private void playRegularTurn() throws IllegalActionException {

        // Sort players according to their role
        List<Player> sortedPlayers=new ArrayList<>(this.players);
        sortedPlayers.sort(Comparator.comparingInt(player -> player.getCurrentRole().getTurnOrder()));

        for (Player player : sortedPlayers) {
            //if this player was killed skip his turn
            SelfPlayerView currentPlayerView = new SelfPlayerView(player);
            RegularTurnAction action = new RegularTurnAction(currentPlayerView, this.deck.peekFirstTwo());

            try {
                player.getBehavior().playTurn(action, currentPlayerView, new GameView(this));
            } catch (Exception e) {
                throw new IllegalActionException("Player failed to play turn", e);
            }

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

    private void determineNextFirstPlayer() {
        Optional<Player> kingPlayer = this.players.stream()
                .filter(player -> player.getCurrentRole() == Role.ROI)
                .findFirst();

        if (kingPlayer.isPresent()) {

            this.firstPlayerIndex = this.players.indexOf(kingPlayer.get());
        } else {

            int roiIndex = this.players.indexOf(kingPlayer.orElse(null));
            int nextPlayerIndex = (roiIndex + 1) % this.players.size();
            this.firstPlayerIndex = nextPlayerIndex;
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