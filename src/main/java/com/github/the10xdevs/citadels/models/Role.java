package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.abilities.*;

/**
 * A role in the game
 */
public enum Role {
    ASSASSIN(1),
    VOLEUR(2),
    MAGICIEN(3),
    ROI(4, Category.NOBLE),
    EVEQUE(5, Category.RELIGIEUX),
    MARCHAND(6, Category.MARCHAND),
    ARCHITECTE(7),
    CONDOTTIERE(8, Category.MILITAIRE);

    private final int turnOrder;
    private final Category category;

    /**
     * Create a new role without a category
     *
     * @param turnOrder The order in which this role play
     */
    Role(int turnOrder) {
        this(turnOrder, null);
    }

    /**
     * Create a new role with a category
     *
     * @param turnOrder The order in which this role play
     * @param category  The category of the role
     */
    Role(int turnOrder, Category category) {
        this.turnOrder = turnOrder;
        this.category = category;
    }

    /**
     * Returns the turn order of this role
     *
     * @return The turn order of this role
     */
    public int getTurnOrder() {
        return this.turnOrder;
    }

    /**
     * Returns the category of this role
     *
     * @return The category of this role
     */
    public Category getCategory() {
        return this.category;
    }

    /**
     * Create a new AbilityAction corresponding to the role
     *
     * @param currentPlayer The current player
     * @param game          The current game
     * @return The ability action
     */
    public AbilityAction getAbilityAction(Player currentPlayer, Game game) {
        return switch (this) {
            case ASSASSIN -> new AssassinAbilityAction(game);
            case VOLEUR -> new VoleurAbilityAction(game);
            case MAGICIEN -> new MagicienAbilityAction(currentPlayer, game);
            case CONDOTTIERE -> new CondottiereAbilityAction(currentPlayer, game);
            case ARCHITECTE -> new ArchitecteAbilityAction(currentPlayer, game);
            default -> null;
        };
    }

    @Override
    public String toString() {
        return switch (this) {
            case ASSASSIN -> "Assassin";
            case VOLEUR -> "Voleur";
            case MAGICIEN -> "Magicien";
            case ROI -> "Roi";
            case EVEQUE -> "Eveque";
            case MARCHAND -> "Marchand";
            case ARCHITECTE -> "Architecte";
            case CONDOTTIERE -> "Condottiere";
        };
    }
}