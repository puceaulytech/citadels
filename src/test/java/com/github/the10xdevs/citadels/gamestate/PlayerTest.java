package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void getScore() throws DuplicatedDistrictException {
        Player player = new Player(null);
        player.getCity().addDistrict(new District("Baraque 1", Category.MERVEILLE, 3));
        player.getCity().addDistrict(new District("Baraque 2", Category.MERVEILLE, 6));

        assertEquals(9, player.getScore(false));
    }

    @Test
    void getScoreFiveDifferentCategories() throws DuplicatedDistrictException {
        Player player = new Player(null);
        player.getCity().addDistrict(new District("Baraque 1", Category.MERVEILLE, 1));
        player.getCity().addDistrict(new District("Baraque 2", Category.MARCHAND, 1));
        player.getCity().addDistrict(new District("Baraque 3", Category.MILITAIRE, 1));
        player.getCity().addDistrict(new District("Baraque 4", Category.NOBLE, 1));
        player.getCity().addDistrict(new District("Baraque 5", Category.RELIGIEUX, 1));

        assertEquals(8, player.getScore(false));
    }

    @Test
    void getScoreEightDistrict() throws DuplicatedDistrictException {
        Player player = new Player(null);
        player.getCity().addDistrict(new District("Baraque 1", Category.MERVEILLE, 1));
        player.getCity().addDistrict(new District("Baraque 2", Category.MARCHAND, 1));
        player.getCity().addDistrict(new District("Baraque 3", Category.MILITAIRE, 1));
        player.getCity().addDistrict(new District("Baraque 4", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 5", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 6", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 7", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 8", Category.RELIGIEUX, 1));

        assertEquals(10, player.getScore(false));
    }

    @Test
    void getScoreEightDistrictFirstPlayer() throws DuplicatedDistrictException {
        Player player = new Player(null);
        player.getCity().addDistrict(new District("Baraque 1", Category.MERVEILLE, 1));
        player.getCity().addDistrict(new District("Baraque 2", Category.MARCHAND, 1));
        player.getCity().addDistrict(new District("Baraque 3", Category.MILITAIRE, 1));
        player.getCity().addDistrict(new District("Baraque 4", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 5", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 6", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 7", Category.RELIGIEUX, 1));
        player.getCity().addDistrict(new District("Baraque 8", Category.RELIGIEUX, 1));

        assertEquals(12, player.getScore(true));
    }
}