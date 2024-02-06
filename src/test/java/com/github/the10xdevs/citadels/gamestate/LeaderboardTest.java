package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class LeaderboardTest {
    @Test
    void correctScores() throws DuplicatedDistrictException {
        Player first = new Player(null);
        first.getCity().addDistrict(new District("Stuff", Category.RELIGIEUX, 10));
        first.getCity().addDistrict(new District("Stuff2", Category.RELIGIEUX, 11));

        Player second = new Player(null);
        second.getCity().addDistrict(new District("Stuff3", Category.RELIGIEUX, 8));

        Leaderboard leaderboard = new Leaderboard(List.of(second, first), null);

        assertEquals(21, leaderboard.getEntries().get(0).getScore());
        assertEquals(first, leaderboard.getEntries().get(0).getPlayer());

        assertEquals(8, leaderboard.getEntries().get(1).getScore());
        assertEquals(second, leaderboard.getEntries().get(1).getPlayer());
    }
}