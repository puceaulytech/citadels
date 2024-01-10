package com.github.the10xdevs.citadels.interaction.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
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
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ExpensiveBuilderBehaviorTest {
    ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
    Set<Role> availableRoles = EnumSet.allOf(Role.class);
    GameView game = new GameView(new Game(List.of()));

    Player testPlayer;
    SelfPlayerView selfTestPlayer;

    @BeforeEach
    void initPlayer() {
        testPlayer = new Player(behavior);
        selfTestPlayer = new SelfPlayerView(testPlayer);
        testPlayer.setCurrentRole(Role.ARCHITECTE);
    }

    @Test
    void testPickRole() {
        RoleTurnAction action = new RoleTurnAction(availableRoles);
        assertDoesNotThrow(() -> behavior.pickRole(action, selfTestPlayer, game, availableRoles));
        // maybe check for preferred role being picked
        assertNotNull(action.getPickedRole());
        assertNotNull(action.getDiscardedRole());
    }

    @Test
    void testPlayTurn() {
        RegularTurnAction action = new RegularTurnAction(selfTestPlayer, new Pair<>(new District("nobleDistrict", Category.NOBLE, 6), new District("ReligiousDistrict", Category.RELIGIEUX, 8)));
        assertDoesNotThrow(() -> behavior.playTurn(action, selfTestPlayer, game));
    }

    @Test
    void testPlayTurnMarkingDistrict() throws IllegalActionException {
        District notGoodEnough = new District("DaHood", Category.MARCHAND, 2);
        District goodButNotBest = new District("NobleDistrict", Category.NOBLE, 7);
        District best = new District("ReligiousDistrict", Category.RELIGIEUX, 8);
        testPlayer.getHand().addAll(List.of(notGoodEnough, goodButNotBest, best));
        testPlayer.incrementGold(10);

        // he should mark a district for the next turn
        RegularTurnAction action = new RegularTurnAction(selfTestPlayer, null);
        behavior.playTurn(action, selfTestPlayer, game);
        assertEquals(RegularTurnAction.BasicAction.GOLD, action.getBasicAction());
        assertNull(action.getBuiltDistrict());

        // he should build the district with the highest score
        action = new RegularTurnAction(selfTestPlayer, null);
        behavior.playTurn(action, selfTestPlayer, game);
        assertEquals(best, action.getBuiltDistrict());
    }
}
