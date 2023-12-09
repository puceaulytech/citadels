package com.github.the10xdevs.citadels.logging;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.VoleurAbilityAction;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.List;

public class ConsoleLogger {
    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_BLACK = "\u001B[30m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_BLUE = "\u001B[34m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_WHITE = "\u001B[37m";
    public static final String ANSI_GOLD = "\u001B[38;2;201;176;55m";
    public static final String ANSI_SILVER = "\u001B[38;2;180;180;180m";
    public static final String ANSI_BRONZE = "\u001B[38;2;106;56;5m";

    private final BufferedOutputStream outputStream = new BufferedOutputStream(System.out);
    private final boolean supportsColor;

    public ConsoleLogger() {
        this.supportsColor = !System.getProperty("os.name").toLowerCase().contains("win");
    }

    /**
     * Log the start of a turn
     *
     * @param turn The turn number
     */
    public void logTurnStart(int turn) {
        this.println();
        this.print("------ Tour n°");
        this.printInt(turn);
        this.println(" ------");

        this.flush();
    }

    /**
     * Log a role choosing action
     *
     * @param index  The index of the player
     * @param player The player
     * @param action The action made by the player
     */
    public void logRoleTurnAction(int index, Player player, RoleTurnAction action) {
        this.println();
        this.print("--- Joueur n°");
        this.printInt(index + 1);
        this.print(" (");
        this.print(player.getBehavior().getName());
        this.println(") ---");
        this.print("Choisit le rôle ");
        this.printColorized(action.getPickedRole());
        this.println();
        if (action.getDiscardedRole() != null) {
            this.print("Défausse le rôle ");
            this.printColorized(action.getDiscardedRole());
            this.println();
        }

        this.flush();
    }

    private void logAbilityAction(Player player, AbilityAction action) {
        if (player.getCurrentRole() == Role.ASSASSIN) {
            AssassinAbilityAction assassinAction = (AssassinAbilityAction) action;
            if (assassinAction.getKilledRole() != null) {
                this.print("Assassine le rôle ");
                this.printColorized(assassinAction.getKilledRole());
                this.println();
            }
        } else if (player.getCurrentRole() == Role.VOLEUR) {
            VoleurAbilityAction voleurAction = (VoleurAbilityAction) action;
            if (voleurAction.getStolenRole() != null) {
                this.print("Vole le rôle ");
                this.printColorized(voleurAction.getStolenRole());
                this.println();
            }
        }
    }

    /**
     * Log a regular turn action
     *
     * @param player The player that made the action
     * @param action The action made by the player
     */
    public void logRegularTurnAction(Player player, RegularTurnAction action) {
        this.println();
        this.print("--- Joueur ayant le rôle ");
        this.printColorized(player.getCurrentRole());
        this.print(" (");
        this.print(player.getBehavior().getName());
        this.println(") ---");

        this.logAbilityAction(player, action.getAbilityAction());

        if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
            this.println("Prend deux pièces d'or");
        } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
            District discarded = action.getDiscardedCard();
            if (discarded != null) {
                this.println("Pioche deux cartes");
                this.print("    garde    ");
                this.printColorized(action.getChosenCard());
                this.println();
                this.print("    défausse ");
                this.printColorized(discarded);
                this.println();
            } else {
                this.println("Pioche une carte");
                this.print("    garde    ");
                this.printColorized(action.getChosenCard());
                this.println();
            }
        }

        if (action.getBuiltDistrict() != null) {
            this.print("Construit le quartier ");
            this.printColorized(action.getBuiltDistrict());
            this.println();
        }

        this.flush();
    }

    /**
     * Log winners of the game
     *
     * @param players All the players
     */
    public void logWinners(List<Player> players) {
        this.println("\n------ Podium ------");
        int rank = 1;
        for (Player player : players) {
            if (this.supportsColor) {
                if (rank == 1) {
                    this.print(ANSI_GOLD);
                } else if (rank == 2) {
                    this.print(ANSI_SILVER);
                } else if (rank == 3) {
                    this.print(ANSI_BRONZE);
                }
            }

            this.print("-> ");
            this.print(player.getBehavior().getName());
            this.print(" avec ");
            this.printInt(player.getScore());
            this.println(" points");

            if (rank <= 3 && this.supportsColor) {
                this.print(ANSI_RESET);
            }

            for (District district : player.getCity()) {
                this.print("  - ");
                this.printColorized(district);
                this.println();
            }
            this.println();
            this.flush();

            rank++;
        }
    }

    /**
     * Log if an error occurs during the game
     *
     * @param error Caught error to be logged
     */
    public void logError(Throwable error) {
        this.println();
        this.println("------ " + ANSI_RED + "Error" + ANSI_RESET + " ------");
        this.println("Something went wrong during play: ");
        error.printStackTrace(new PrintWriter(this.outputStream, true));
        this.flush();
    }

    private void println() {
        try {
            outputStream.write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            this.unexpectedExit(e);
        }
    }

    private void println(String text) {
        try {
            outputStream.write(text.getBytes());
            outputStream.write(System.lineSeparator().getBytes());
        } catch (IOException e) {
            this.unexpectedExit(e);
        }
    }

    private void printInt(int value) {
        try {
            outputStream.write(String.valueOf(value).getBytes());
        } catch (IOException e) {
            this.unexpectedExit(e);
        }
    }

    private void print(String text) {
        try {
            outputStream.write(text.getBytes());
        } catch (IOException e) {
            this.unexpectedExit(e);
        }
    }

    private void flush() {
        try {
            outputStream.flush();
        } catch (IOException e) {
            this.unexpectedExit(e);
        }
    }

    private void printColorized(Role role) {
        Category category = role.getCategory();
        if (category != null && this.supportsColor)
            this.print(role.getCategory().getANSIColorCode());

        this.print(role.toString());

        if (category != null && this.supportsColor)
            this.print(ANSI_RESET);
    }

    private void printColorized(District district) {
        this.print(district.getName());
        this.print(", ");

        if (this.supportsColor)
            this.print(district.getCategory().getANSIColorCode());

        this.print(district.getCategory().toString());

        if (this.supportsColor)
            this.print(ANSI_RESET);

        this.print(", prix: ");
        this.printInt(district.getCost());
    }

    private void unexpectedExit(Throwable e) {
        System.out.println("Error on stdout: " + e);
        System.exit(1);
    }
}
