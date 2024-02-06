package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.logging.ConsoleLogger;
import com.github.the10xdevs.citadels.logging.Logger;
import com.github.the10xdevs.citadels.models.District;

import java.util.ArrayList;
import java.util.List;

public class GameBuilder {
    private final List<Behavior> behaviors = new ArrayList<>();
    private Logger logger = new ConsoleLogger();
    private Deck<District> deck = new Deck<>(District.all());

    private GameBuilder() {
    }

    public static GameBuilder create() {
        return new GameBuilder();
    }

    public GameBuilder withLogger(Logger logger) {
        this.logger = logger;
        return this;
    }

    public GameBuilder withDeck(Deck<District> deck) {
        this.deck = deck;
        return this;
    }

    public GameBuilder addBehavior(Behavior behavior) {
        this.behaviors.add(behavior);
        return this;
    }

    public Game build() {
        return new Game(this.behaviors, this.deck, this.logger);
    }
}
