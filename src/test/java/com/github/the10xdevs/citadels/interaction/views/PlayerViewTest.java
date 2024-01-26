package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.interaction.actions.RegularTurnAction;
import com.github.the10xdevs.citadels.interaction.actions.RoleTurnAction;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class PlayerViewTest {

    @Test
    void getGold() {
        Player player = new Player(null);

        player.setGold(10); // Set the gold value

        PlayerView playerView = new PlayerView(player);

        assertEquals(10, playerView.getGold());
    }
    @Test
    void represents() {
        Behavior behavior1 = mock(Behavior.class);
        Behavior behavior2 = mock(Behavior.class);

        Player player1 = new Player(behavior1);
        Player player2 = new Player(behavior2);

        PlayerView playerView1 = new PlayerView(player1);
        PlayerView playerView2 = new PlayerView(player2);

        assertTrue(playerView1.represents(player1));
        assertFalse(playerView1.represents(player2));
        assertFalse(playerView2.represents(player1));
        assertTrue(playerView2.represents(player2));
    }
    @Test
    void roleHidden() {
        Player p = new Player(null);
        p.setCurrentRole(Role.ROI);

        PlayerView view = new PlayerView(p, false);
        assertNull(view.getCurrentRole());
    }

    @Test
    void roleKnownToAll() {
        Player p = new Player(null);
        p.setCurrentRole(Role.ROI);

        PlayerView view = new PlayerView(p, true);
        assertEquals(Role.ROI, view.getCurrentRole());
    }
}