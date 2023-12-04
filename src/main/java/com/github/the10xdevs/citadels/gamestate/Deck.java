package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.utils.Pair;

import java.util.*;

public class Deck {
    private final Deque<District> districts = new ArrayDeque<>();

    public Deck(Collection<District> cards) {
        this.districts.addAll(cards);
    }

    public void shuffle() {
        List<District> elements = new ArrayList<>(this.districts);
        Collections.shuffle(elements);
        this.districts.clear();
        this.districts.addAll(elements);
    }

    public District drawCard() {
        return this.districts.poll();
    }

    public void enqueueCard(District district) {
        this.districts.offer(district);
    }

    public int getCardsCount() {
        return this.districts.size();
    }

    public Pair<District, District> peekFirstTwo() {
        Iterator<District> iterator = this.districts.iterator();
        return new Pair<>(iterator.next(), iterator.next());
    }
}
