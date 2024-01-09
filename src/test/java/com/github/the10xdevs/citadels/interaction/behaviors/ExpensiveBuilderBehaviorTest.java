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
import org.junit.jupiter.api.Test;
import java.util.EnumSet;
import java.util.List;
import java.util.Set;
import static org.junit.jupiter.api.Assertions.*;

class ExpensiveBuilderBehaviorTest {

    @Test
    void testPickRole() throws IllegalActionException {
        ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
        RoleTurnAction action = new RoleTurnAction(EnumSet.allOf(Role.class));
        SelfPlayerView self = new SelfPlayerView(new Player(new ExpensiveBuilderBehavior()));
        GameView game = new GameView(new Game(List.of()));
        Set<Role> availableRoles = EnumSet.allOf(Role.class);

        assertDoesNotThrow(() -> behavior.pickRole(action, self, game, availableRoles));
        assertNotNull(action.getPickedRole());
        assertNotNull(action.getDiscardedRole());
    }

    @Test
    void testPlayTurn() throws IllegalActionException {
        ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
        Set<Role> availableRoles = EnumSet.allOf(Role.class);
        RoleTurnAction roleTurnAction = new RoleTurnAction(availableRoles);
        RegularTurnAction action = new RegularTurnAction(new SelfPlayerView(new Player(new ExpensiveBuilderBehavior())), new Pair<>(new District("nobleDistrict", Category.NOBLE, 6), new District("ReligiousDistrict", Category.RELIGIEUX, 8)));
        SelfPlayerView self = new SelfPlayerView(new Player(new ExpensiveBuilderBehavior()));
        GameView game = new GameView(new Game(List.of()));
        behavior.pickRole(roleTurnAction, self, game, availableRoles);
    }
    @Test
    void testPlayTurnWithMarkedDistrict() throws IllegalActionException {
        ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
        Set<Role> availableRoles = EnumSet.allOf(Role.class);
        RoleTurnAction roleTurnAction = new RoleTurnAction(availableRoles);
        RegularTurnAction action = new RegularTurnAction(new SelfPlayerView(new Player(new ExpensiveBuilderBehavior())), new Pair<>(new District("nobleDistrict", Category.NOBLE, 6), new District("ReligiousDistrict", Category.RELIGIEUX, 8)));
        SelfPlayerView self = new SelfPlayerView(new Player(new ExpensiveBuilderBehavior()));
        GameView game = new GameView(new Game(List.of()));
        behavior.pickRole(roleTurnAction, self, game, availableRoles);

        // Mark a district for building
        District markedDistrict = new District("ExpensiveDistrict", Category.NOBLE, 10);
        behavior.playTurn(action, self, game);
        assertEquals(markedDistrict, action.getBuiltDistrict());

        // Try to build the marked district
        behavior.playTurn(action, self, game);
        assertNull(action.getChosenCard());  // No card should be chosen
        assertNull(action.getBuiltDistrict());  // No district should be built
        assertEquals(Role.CONDOTTIERE, action.getChosenCard());  // Confirm the role picked after building

        // Add more assertions as needed
    }

    @Test
    void testPlayTurnWithoutMarkedDistrict() throws IllegalActionException {
        ExpensiveBuilderBehavior behavior = new ExpensiveBuilderBehavior();
        Set<Role> availableRoles = EnumSet.allOf(Role.class);
        RoleTurnAction roleTurnAction = new RoleTurnAction(availableRoles);
        RegularTurnAction action = new RegularTurnAction(new SelfPlayerView(new Player(new ExpensiveBuilderBehavior())), new Pair<>(new District("nobleDistrict", Category.NOBLE, 6), new District("ReligiousDistrict", Category.RELIGIEUX, 8)));
        SelfPlayerView self = new SelfPlayerView(new Player(new ExpensiveBuilderBehavior()));
        GameView game = new GameView(new Game(List.of()));
        behavior.pickRole(roleTurnAction, self, game, availableRoles);

        // No marked district, so take gold
        behavior.playTurn(action, self, game);
        assertNull(action.getChosenCard());
        assertNull(action.getBuiltDistrict());

        // Add more assertions as needed
    }
}
