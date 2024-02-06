package com.github.the10xdevs.citadels.logging;

import com.github.the10xdevs.citadels.gamestate.Leaderboard;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.*;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;

import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Optional;

public class ConsoleLogger implements Logger {
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

    @Override
    public void logTurnStart(int turn) {
        this.println();
        this.print("------ Tour n°");
        this.printInt(turn);
        this.println(" ------");

        this.flush();
    }

    @Override
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
        } else if (player.getCurrentRole() == Role.MAGICIEN) {
            MagicienAbilityAction magicienAction = (MagicienAbilityAction) action;
            if (magicienAction.getExchangedPlayer() != null) {
                this.print("Echange ses cartes avec ");
                this.println(magicienAction.getExchangedPlayer().getName());
            }
        } else if (player.getCurrentRole() == Role.CONDOTTIERE) {
            CondottiereAbilityAction condottiereAction = (CondottiereAbilityAction) action;
            if (condottiereAction.getTargetDistrict() != null) {
                this.print("Détruit ");
                this.printColorized(condottiereAction.getTargetDistrict());
                this.print(" chez ");
                this.println(condottiereAction.getTargetPlayer().getName());
            }
        }
    }

    @Override
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
            Optional<District> discarded = action.getDiscardedCard();
            if (discarded.isPresent()) {
                this.println("Pioche deux cartes");
                this.print("    garde    ");
                this.printColorized(action.getChosenCard());
                this.println();
                this.print("    défausse ");
                this.printColorized(discarded.get());
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

    @Override
    public void logWinners(Leaderboard leaderboard) {
        this.println("\n------ Podium ------");
        int rank = 1;
        for (Leaderboard.Entry entry : leaderboard.getEntries()) {
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
            this.print(entry.getPlayer().getBehavior().getName());
            this.print(" avec ");
            this.printInt(entry.getScore());
            this.println(" points");

            if (rank <= 3 && this.supportsColor) {
                this.print(ANSI_RESET);
            }

            for (District district : entry.getPlayer().getCity()) {
                this.print("  - ");
                this.printColorized(district);
                this.println();
            }
            this.println();
            this.flush();

            rank++;
        }
    }

    @Override
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

        if (district.getCost() != district.getScore()) {
            this.print(", score: ");
            this.printInt(district.getScore());
        }
    }

    private void unexpectedExit(Throwable e) {
        System.out.println("Error on stdout: " + e);
        System.exit(1);
    }
}
