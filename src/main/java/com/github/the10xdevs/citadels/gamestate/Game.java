package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.logging.Logger;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.util.*;

/**
 * The main class
 */
public class Game {
    private final List<Player> players = new ArrayList<>();
    private final Deck<District> deck;
    private final Logger logger;
    private final Set<Role> rolesFacingUp = EnumSet.noneOf(Role.class);
    private int firstPlayerIndex = 0;
    private int turn = 1;
    private int currentTurnOrder;
    private Role killedRole;
    private Role stolenRole;

    /**
     * Constructs a Game with a list of Behaviors that will battle against each other
     *
     * @param behaviors The list of Behaviors
     */
    public Game(List<Behavior> behaviors, Deck<District> deck, Logger logger) {
        this.deck = deck;
        this.logger = logger;

        for (Behavior behavior : behaviors) {
            players.add(new Player(behavior));
        }
    }

    /**
     * Starts the game
     */
    public Leaderboard start() {
        this.deck.shuffle();

        // Give four cards and two gold to each player
        for (Player player : this.players) {
            player.incrementGold(2);
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

        Leaderboard leaderboard = new Leaderboard(this.players, firstPlayerToFinish);
        this.logger.logWinners(leaderboard);

        return leaderboard;
    }

    /**
     * Checks if the game has ended
     *
     * @return true if the game is over, false otherwise
     */
    public boolean isGameOver() {
        for (Player player : players) {
            if (player.getCity().getSize() >= 8) {
                return true;
            }
        }
        return false;
    }

    /**
     * Initializes a new turn
     */
    private void initTurn() {
        this.killedRole = null;
        this.stolenRole = null;
        this.currentTurnOrder = 0;
    }

    private Role drawUntilNotKing(Deck<Role> deck) {
        Role r = deck.drawCard();
        while (r == Role.ROI) {
            deck.enqueueCard(r);
            r = deck.drawCard();
        }
        return r;
    }

    /**
     * Makes all the players choose their role
     *
     * @throws IllegalActionException if a player has performed an action that is not permitted
     */
    private void playRoleTurn() throws IllegalActionException {
        // Create a deck all available roles
        Deck<Role> roles = new Deck<>(Arrays.asList(Role.values()));
        roles.shuffle();
        // Put a random role facing down
        Role roleFacingDown = roles.drawCard();
        // Clear roles facing up from last turn
        this.rolesFacingUp.clear();
        // Put 0, 1 or 2 roles facing up depending on the number of players
        if (this.players.size() == 5) {
            this.rolesFacingUp.add(this.drawUntilNotKing(roles));
        }
        if (this.players.size() == 4) {
            this.rolesFacingUp.add(this.drawUntilNotKing(roles));
            this.rolesFacingUp.add(this.drawUntilNotKing(roles));
        }

        for (int i = 0; i < this.players.size(); i++) {
            // Get next player to play
            Player player = this.players.get((i + firstPlayerIndex) % this.players.size());

            Set<Role> availableRoles = EnumSet.copyOf(roles.getElements());
            // If this is the turn of the seventh player, add the card facing down
            if (i == 6) {
                availableRoles.add(roleFacingDown);
            }
            RoleTurnAction roleTurnAction = new RoleTurnAction(availableRoles);

            try {
                player.getBehavior().pickRole(roleTurnAction, new SelfPlayerView(player), new GameView(this));
            } catch (Exception e) {
                throw new IllegalActionException("Player failed to pick role", e);
            }

            this.logger.logRoleTurnAction(i, player, roleTurnAction);

            player.setCurrentRole(roleTurnAction.getPickedRole());
            if (this.players.size() == 2)
                roles.remove(roleTurnAction.getDiscardedRole());
            roles.remove(roleTurnAction.getPickedRole()); // if the 7th player chose the card facing down nothing happens here
        }
    }

    /**
     * Computes the amount of gold gained by a player according to the number of districts in his city
     * matching his current role
     *
     * @param player The player
     * @return The amount of gold the player is supposed to gain
     */
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

        // Check for School of Magic
        District schoolOfMagic = new District("Ecole de magie", Category.MERVEILLE, 6);
        if (player.getCurrentRole().getCategory() != null && player.getCity().getDistricts().contains(schoolOfMagic)) {
            goldReward += 2;
        }

        return goldReward;
    }

    /**
     * Makes all the players play their turn
     *
     * @throws IllegalActionException if a player has performed an action that is not permitted
     */
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

            this.currentTurnOrder = player.getCurrentRole().getTurnOrder();

            SelfPlayerView currentPlayerView = new SelfPlayerView(player);
            RegularTurnAction action = new RegularTurnAction(this, player);

            try {
                player.getBehavior().playTurn(action, currentPlayerView, new GameView(this));
            } catch (Exception e) {
                throw new IllegalActionException("Player failed to play turn", e);
            }

            this.logger.logRegularTurnAction(player, action);
        }
    }

    /**
     * Determines who is the next player to play first
     */
    private void determineNextFirstPlayer() {
        Optional<Player> kingPlayer = this.players.stream()
                .filter(player -> player.getCurrentRole() == Role.ROI)
                .findFirst();

        kingPlayer.ifPresent(player -> this.firstPlayerIndex = this.players.indexOf(player));
    }

    /**
     * Returns the list of players
     *
     * @return The list of players
     */
    public List<Player> getPlayers() {
        return players;
    }

    /**
     * Returns the deck
     *
     * @return The deck
     */
    public Deck<District> getDeck() {
        return deck;
    }

    /**
     * Returns which turn is currently being played
     *
     * @return The turn currently being played
     */
    public int getTurn() {
        return turn;
    }

    public int getCurrentTurnOrder() {
        return this.currentTurnOrder;
    }

    public Set<Role> getRolesFacingUp() {
        return this.rolesFacingUp;
    }

    public Optional<Role> getKilledRole() {
        return Optional.ofNullable(this.killedRole);
    }

    public void setKilledRole(Role killedRole) {
        this.killedRole = killedRole;
    }

    public Optional<Role> getStolenRole() {
        return Optional.ofNullable(this.stolenRole);
    }

    public void setStolenRole(Role stolenRole) {
        this.stolenRole = stolenRole;
    }

}