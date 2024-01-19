package com.github.the10xdevs.citadels.interaction.views;

import com.github.the10xdevs.citadels.gamestate.Player;
import com.github.the10xdevs.citadels.models.Role;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerViewTest {
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