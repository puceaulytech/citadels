package com.github.the10xdevs.citadels.behaviors;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.interaction.behaviors.DummyBehavior;
import com.github.the10xdevs.citadels.interaction.views.GameView;
import com.github.the10xdevs.citadels.interaction.views.SelfPlayerView;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.HashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

public class DummyBehaviorTest {
    @BeforeEach
    void setUp() {
        SelfPlayerView self =new SelfPlayerView(new Player(new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
                availableRoles.add(Role.EVEQUE);
                availableRoles.add(Role.MAGICIEN);
                availableRoles.add(Role.CONDOTTIERE);
                availableRoles.add(Role.ROI);
                action.pick(Role.ROI);
                action.discard(Role.MAGICIEN);
            }
            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {
            }
        }));
    }

    @Test
    void pickRoleTest() {
        DummyBehavior dummyBehavior = new DummyBehavior();

        // Set up available roles
        Set<Role> availableRoles = new HashSet<>();
        availableRoles.add(Role.ROI);
        availableRoles.add(Role.MARCHAND);

        // Create a RoleTurnAction
        RoleTurnAction roleTurnAction = new RoleTurnAction();

        // Call pickRole method
        assertDoesNotThrow(() -> dummyBehavior.pickRole(roleTurnAction, availableRoles));

        // Check if the picked and discarded roles are valid
        assertTrue(availableRoles.contains(roleTurnAction.getPickedRole()));
        assertTrue(availableRoles.contains(roleTurnAction.getDiscardedRole()));
        assertNotEquals(roleTurnAction.getPickedRole(), roleTurnAction.getDiscardedRole());
    }

    /***
    @Test
    void playTurnTest() {


        DummyBehavior dummyBehavior = new DummyBehavior();

        // Set up a RegularTurnAction
        RegularTurnAction regularTurnAction = new RegularTurnAction(new SelfPlayerView(new Player(new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) {
                availableRoles.add(Role.EVEQUE);
                availableRoles.add(Role.MAGICIEN);
                availableRoles.add(Role.CONDOTTIERE);
                availableRoles.add(Role.ROI);
                action.pick(Role.ROI);
                action.discard(Role.MAGICIEN);
            }
            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) {

            }
        })), null);

        // Call playTurn method
        assertDoesNotThrow(() -> dummyBehavior.playTurn(regularTurnAction, new SelfPlayerView(new Player(new Behavior() {
            @Override
            public void pickRole(RoleTurnAction action, Set<Role> availableRoles) throws IllegalActionException {

            }

            @Override
            public void playTurn(RegularTurnAction action, SelfPlayerView self, GameView gameState) throws IllegalActionException {

            }
        })), null));

        // Check if the basic action is valid
        assertNotNull(regularTurnAction.getBasicAction());
        assertTrue(regularTurnAction.getBasicAction() == RegularTurnAction.BasicAction.GOLD ||
                regularTurnAction.getBasicAction() == RegularTurnAction.BasicAction.CARDS);
    } ***/
}
