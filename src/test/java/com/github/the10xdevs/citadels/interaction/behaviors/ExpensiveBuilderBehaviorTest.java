package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Deck;
import com.github.the10xdevs.citadels.gamestate.Game;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import com.github.the10xdevs.citadels.models.Role;
import com.github.the10xdevs.citadels.utils.Pair;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.Collections;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ExpensiveBuilderBehaviorTest {
    ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
    Game game = new Game(List.of());

    Player testPlayer;
    SelfPlayerView selfTestPlayer;

    @BeforeEach
    void initPlayer() {
        testPlayer = new Player(behavior);
        selfTestPlayer = new SelfPlayerView(testPlayer);
        testPlayer.setCurrentRole(Role.ARCHITECTE);
    }

    @ParameterizedTest
    @MethodSource("com.github.the10xdevs.citadels.interaction.behaviors.BehaviorTestUtils#generateRoles")
    void pickRoleTest(Set<Role> availableRoles) {
        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction(Collections.unmodifiableSet(availableRoles));

        // Call pickRole method
        assertDoesNotThrow(() -> behavior.pickRole(roleTurnAction, null, null, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    @Test
    void testPlayTurn() {
        Deck deck = new Deck(List.of(new District("nobleDistrict", Category.NOBLE, 6), new District("ReligiousDistrict", Category.RELIGIEUX, 8)));
        RegularTurnAction action = new RegularTurnAction(game, testPlayer, deck);
        assertDoesNotThrow(() -> behavior.playTurn(action, selfTestPlayer, new GameView(game)));
    }

    @Test
    void testPlayTurnMarkingDistrict() throws IllegalActionException {
        Deck deck = new Deck(List.of());

        District notGoodEnough = new District("DaHood", Category.MARCHAND, 2);
        District goodButNotBest = new District("NobleDistrict", Category.NOBLE, 7);
        District best = new District("ReligiousDistrict", Category.RELIGIEUX, 8);
        testPlayer.getHand().addAll(List.of(notGoodEnough, goodButNotBest, best));
        testPlayer.incrementGold(10);

        // he should mark a district for the next turn
        RegularTurnAction action = new RegularTurnAction(game, testPlayer, deck);
        behavior.playTurn(action, selfTestPlayer, new GameView(game));
        assertEquals(RegularTurnAction.BasicAction.GOLD, action.getBasicAction());
        assertNull(action.getBuiltDistrict());

        // he should build the district with the highest score
        action = new RegularTurnAction(game, testPlayer, deck);
        behavior.playTurn(action, selfTestPlayer, new GameView(game));
        assertEquals(best, action.getBuiltDistrict());
    }
}
