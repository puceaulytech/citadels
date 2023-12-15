package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
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

    private Role killedRole;
    private Role stolenRole;

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
                this.initTurn();
                this.logger.logTurnStart(this.turn);
                this.playRoleTurn();
                this.playRegularTurn();
                this.determineNextFirstPlayer();
                this.turn++;
            }
        } catch (IllegalActionException e) {
            this.logger.logError(e);
            System.exit(1);
        }

        Player firstPlayerToFinish = this.players.stream()
                .sorted(Comparator.comparingInt(player -> player.getCurrentRole().getTurnOrder()))
                .filter(player -> player.getCity().getSize() == 8)
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("Game is finished but no player has eight built districts"));

        // At the end of the game, sort players by their score (sum of all their district's cost)
        this.players.sort(Comparator.comparingInt((Player player) -> player.getScore(player == firstPlayerToFinish)).reversed());
        this.logger.logWinners(this.players, firstPlayerToFinish);
    }

    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getCity().getSize() >= 8) {
                return true;
            }
        }
        return false;
    }

    private void initTurn() {
        this.killedRole = null;
        this.stolenRole = null;
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

    private int checkMatchingDistricts(Player player) {
        int goldReward = 0;
        Role playerRole = player.getCurrentRole();

        // Iterate through player's city districts
        for (District district : player.getCity().getDistricts()) {
            // Check if the district's category matches the player's role
            if (district.getCategory() == playerRole.getCategory()) {
                // Reward the player with gold (you can adjust the amount as needed)
                goldReward += 2; // For example, reward 2 gold for each matching district
            }
        }

        return goldReward;
    }

    private void playRegularTurn() throws IllegalActionException {
        // Sort players according to their role
        List<Player> sortedPlayers = new ArrayList<>(this.players);
        sortedPlayers.sort(Comparator.comparingInt(player -> player.getCurrentRole().getTurnOrder()));

        for (Player player : sortedPlayers) {
            // If this payer was killed, skip his turn
            if (player.getCurrentRole() == this.killedRole)
                continue;

            // If this player was stolen, give his gold to the thief
            if (player.getCurrentRole() == this.stolenRole) {
                // Find the thief
                Optional<Player> thief = this.players.stream()
                                .filter(p -> p.getCurrentRole() == Role.VOLEUR)
                                .findFirst();

                if (thief.isPresent()) {
                    // Give the stolen player's gold to the thief
                    thief.get().incrementGold(player.getGold());
                    player.setGold(0);
                } else {
                    throw new IllegalStateException("A player was stolen but there is no thief");
                }
            }

            if (player.getCurrentRole() == Role.MARCHAND) {
                player.incrementGold(1);
            }
          
            // Give a gold reward to the player
            int goldReward = this.checkMatchingDistricts(player);
            player.incrementGold(goldReward);

            SelfPlayerView currentPlayerView = new SelfPlayerView(player);
            RegularTurnAction action = new RegularTurnAction(currentPlayerView, this.deck.peekFirstTwo());

            try {
                player.getBehavior().playTurn(action, currentPlayerView, new GameView(this));
            } catch (Exception e) {
                throw new IllegalActionException("Player failed to play turn", e);
            }

            this.logger.logRegularTurnAction(player, action);

            this.applyRegularTurnAction(player, action);
        }
    }


    private void applyRegularTurnAction(Player player, RegularTurnAction action) throws IllegalActionException {
        // Apply abilities
        if (player.getCurrentRole() == Role.ASSASSIN) {
            AssassinAbilityAction assassinAction = (AssassinAbilityAction) action.getAbilityAction();
            this.killedRole = assassinAction.getKilledRole();
        } else if (player.getCurrentRole() == Role.VOLEUR) {
            VoleurAbilityAction voleurAction = (VoleurAbilityAction) action.getAbilityAction();

            if (voleurAction.getStolenRole() != this.killedRole) {
                this.stolenRole = voleurAction.getStolenRole();
            }
        }

        // Apply gold or deck drawing
        if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
            player.incrementGold(2);
        } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
            // To arrive here there is necessarily at least one card in the deck,
            // so we can safely draw one card
            this.deck.drawCard();
            player.getHand().add(action.getChosenCard());
            if (!deck.isEmpty()) {
                this.deck.drawCard();
                this.deck.enqueueCard(action.getDiscardedCard());
            }
        }

        // Apply district building
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

    private void determineNextFirstPlayer() {
        Optional<Player> kingPlayer = this.players.stream()
                .filter(player -> player.getCurrentRole() == Role.ROI)
                .findFirst();

        kingPlayer.ifPresent(player -> this.firstPlayerIndex = this.players.indexOf(player));
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
