package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.interaction.actions.abilities.AbilityAction;
import com.github.the10xdevs.citadels.interaction.actions.abilities.AssassinAbilityAction;

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

    Role(int turnOrder) {
        this(turnOrder, null);
    }

    Role(int turnOrder, Category category) {
        this.turnOrder = turnOrder;
        this.category = category;
    }

    public int getTurnOrder() { return this.turnOrder; }

    public Category getCategory() { return this.category; }

    public String getRoleName() {
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

    public AbilityAction getAbilityAction() {
        return switch (this) {
            case ASSASSIN -> new AssassinAbilityAction();
            default -> null;
        };
    }
    @Override
    public String toString() {
        return String.format(" %d, %s", this.turnOrder, this.getRoleName());
    }
}
