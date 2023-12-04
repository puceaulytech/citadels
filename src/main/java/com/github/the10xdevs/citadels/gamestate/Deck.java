package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.District;

import java.util.*;

public class Deck {
    private final Deque<District> districts = new ArrayDeque<>(District.all);

    public void shuffle() {
        List<District> elements = new ArrayList<>(this.districts);
        Collections.shuffle(elements);
        this.districts.clear();
        this.districts.addAll(elements);
    }

    public District pollCard() {
        return this.districts.pollLast();
    }

    public void enqueueCard(District district) {
        this.districts.offerFirst(district);
    }

    public int getCardsCount() {
        return this.districts.size();
    }
}
