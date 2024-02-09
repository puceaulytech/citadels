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

    private final PrintWriter writer = new PrintWriter(new BufferedOutputStream(System.out));

    @Override
    public void logTurnStart(int turn) {
        writer.println();
        writer.print("------ Tour n°");
        writer.print(turn);
        writer.println(" ------");

        writer.flush();
    }

    @Override
    public void logRoleTurnAction(int index, Player player, RoleTurnAction action) {
        writer.println();
        writer.print("--- Joueur n°");
        writer.print(index + 1);
        writer.print(" (");
        writer.print(player.getBehavior().getName());
        writer.println(") ---");
        writer.print("Choisit le rôle ");
        this.printColorized(action.getPickedRole());
        writer.println();
        if (action.getDiscardedRole() != null) {
            writer.print("Défausse le rôle ");
            this.printColorized(action.getDiscardedRole());
            writer.println();
        }

        writer.flush();
    }

    private void logAbilityAction(Player player, AbilityAction action) {
        if (player.getCurrentRole() == Role.ASSASSIN) {
            AssassinAbilityAction assassinAction = (AssassinAbilityAction) action;
            if (assassinAction.getKilledRole() != null) {
                writer.print("Assassine le rôle ");
                this.printColorized(assassinAction.getKilledRole());
                writer.println();
            }
        } else if (player.getCurrentRole() == Role.VOLEUR) {
            VoleurAbilityAction voleurAction = (VoleurAbilityAction) action;
            if (voleurAction.getStolenRole() != null) {
                writer.print("Vole le rôle ");
                this.printColorized(voleurAction.getStolenRole());
                writer.println();
            }
        } else if (player.getCurrentRole() == Role.MAGICIEN) {
            MagicienAbilityAction magicienAction = (MagicienAbilityAction) action;
            if (magicienAction.getExchangedPlayer() != null) {
                writer.print("Echange ses cartes avec ");
                writer.println(magicienAction.getExchangedPlayer().getName());
            }
        } else if (player.getCurrentRole() == Role.CONDOTTIERE) {
            CondottiereAbilityAction condottiereAction = (CondottiereAbilityAction) action;
            if (condottiereAction.getTargetDistrict() != null) {
                writer.print("Détruit ");
                this.printColorized(condottiereAction.getTargetDistrict());
                writer.print(" chez ");
                writer.println(condottiereAction.getTargetPlayer().getName());
            }
        } else if (player.getCurrentRole() == Role.ARCHITECTE) {
            ArchitecteAbilityAction architecteAction = (ArchitecteAbilityAction) action;
            if (!architecteAction.getBuiltDistricts().isEmpty()) {
                for (District builtDistrict : architecteAction.getBuiltDistricts()) {
                    writer.print("Construit ");
                    this.printColorized(builtDistrict);
                    writer.println();
                }
            }

            if (!architecteAction.getDrawnCards().isEmpty()) {
                for (District drawnDistrict : architecteAction.getBuiltDistricts()) {
                    writer.print("Pioche ");
                    this.printColorized(drawnDistrict);
                    writer.println();
                }
            }
        }
    }

    @Override
    public void logRegularTurnAction(Player player, RegularTurnAction action) {
        writer.println();
        writer.print("--- Joueur ayant le rôle ");
        this.printColorized(player.getCurrentRole());
        writer.print(" (");
        writer.print(player.getBehavior().getName());
        writer.println(") ---");

        this.logAbilityAction(player, action.getAbilityAction());

        if (action.getBasicAction() == RegularTurnAction.BasicAction.GOLD) {
            writer.println("Prend deux pièces d'or");
        } else if (action.getBasicAction() == RegularTurnAction.BasicAction.CARDS) {
            Optional<District> discarded = action.getDiscardedCard();
            if (discarded.isPresent()) {
                writer.println("Pioche deux cartes");
                writer.print("    garde    ");
                this.printColorized(action.getChosenCard());
                writer.println();
                writer.print("    défausse ");
                this.printColorized(discarded.get());
                writer.println();
            } else {
                writer.println("Pioche une carte");
                writer.print("    garde    ");
                this.printColorized(action.getChosenCard());
                writer.println();
            }
        }

        if (action.getBuiltDistrict() != null) {
            writer.print("Construit le quartier ");
            this.printColorized(action.getBuiltDistrict());
            writer.println();
        }

        writer.flush();
    }

    @Override
    public void logWinners(Leaderboard leaderboard) {
        writer.println("\n------ Podium ------");
        int rank = 1;
        for (Leaderboard.Entry entry : leaderboard.getEntries()) {
            if (rank == 1) {
                writer.print(ANSI_GOLD);
            } else if (rank == 2) {
                writer.print(ANSI_SILVER);
            } else if (rank == 3) {
                writer.print(ANSI_BRONZE);
            }

            writer.print("-> ");
            writer.print(entry.getPlayer().getBehavior().getName());
            writer.print(" avec ");
            writer.print(entry.getScore());
            writer.println(" points");

            if (rank <= 3) {
                writer.print(ANSI_RESET);
            }

            for (District district : entry.getPlayer().getCity()) {
                writer.print("  - ");
                this.printColorized(district);
                writer.println();
            }
            writer.println();
            writer.flush();

            rank++;
        }
    }

    @Override
    public void logError(Throwable error) {
        writer.println();
        writer.println("------ " + ANSI_RED + "Error" + ANSI_RESET + " ------");
        writer.println("Something went wrong during play: ");
        error.printStackTrace(this.writer);
        writer.flush();
    }

    private void printColorized(Role role) {
        Category category = role.getCategory();
        if (category != null)
            writer.print(role.getCategory().getANSIColorCode());

        writer.print(role);

        if (category != null)
            writer.print(ANSI_RESET);
    }

    private void printColorized(District district) {
        writer.print(district.getName());
        writer.print(", ");

        writer.print(district.getCategory().getANSIColorCode());

        writer.print(district.getCategory());

        writer.print(ANSI_RESET);

        writer.print(", prix: ");
        writer.print(district.getCost());

        if (district.getCost() != district.getScore()) {
            writer.print(", score: ");
            writer.print(district.getScore());
        }
    }
}
