package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.District;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Deck {
    private final List<District> districts = new ArrayList<>(District.all);

    public Deck() {
        Collections.shuffle(this.districts);
    }

    /**
     * Take a card from the deck and remove it
     * @return The card
     */
    public District popCard() {
        return this.districts.remove(this.districts.size() - 1);
    }

    public int getCardsCount() {
        return this.districts.size();
    }
}
