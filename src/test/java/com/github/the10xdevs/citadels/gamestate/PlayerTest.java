package com.github.the10xdevs.citadels.gamestate;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import com.github.the10xdevs.citadels.exceptions.IllegalActionException;
import com.github.the10xdevs.citadels.interaction.behaviors.Behavior;
import com.github.the10xdevs.citadels.models.Category;
import com.github.the10xdevs.citadels.models.District;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;

import java.util.Collections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

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

    @Test
    void swapHandWith() {
        Player first = new Player(null);
        Player second = new Player(null);

        District a = new District("Baraque 1", Category.MERVEILLE, 1);
        District b = new District("Baraque 2", Category.MERVEILLE, 1);
        District c = new District("Baraque 3", Category.MERVEILLE, 1);
        District d = new District("Baraque 4", Category.MERVEILLE, 1);

        first.getHand().add(a);
        first.getHand().add(b);

        second.getHand().add(c);
        second.getHand().add(d);

        first.swapHandWith(second);

        assertEquals(a, second.getHand().get(0));
        assertEquals(b, second.getHand().get(1));

        assertEquals(c, first.getHand().get(0));
        assertEquals(d, first.getHand().get(1));
    }


    @Test
    void incrementGoldIncreasesPlayerGold() {
        Player player = new Player(null);
        player.incrementGold(5);
        assertEquals(5, player.getGold());
    }

    @Test
    void buildDistrictThrowsExceptionWhenDistrictIsNull() {
        Player player = new Player(null);
        assertThrows(IllegalActionException.class, () -> {
            player.buildDistrict(null);
        });
    }

    @Test
    void buildDistrictThrowsExceptionWhenDistrictIsNotInHand() {
        Player player = new Player(null);
        District district = new District("Baraque 1", Category.MERVEILLE, 1);
        assertThrows(IllegalActionException.class, () -> {
            player.buildDistrict(district);
        });
    }

    @Test
    void buildDistrictThrowsExceptionWhenNotEnoughGold() {
        Player player = new Player(null);
        District district = new District("Baraque 1", Category.MERVEILLE, 5);
        player.getHand().add(district);
        assertThrows(IllegalActionException.class, () -> {
            player.buildDistrict(district);
        });
    }

    @Test
    void buildDistrictDecreasesGoldAndRemovesDistrictFromHand() throws IllegalActionException {
        Player player = new Player(null);
        District district = new District("Baraque 1", Category.MERVEILLE, 1);
        player.getHand().add(district);
        player.setGold(5);
        player.buildDistrict(district);
        assertEquals(4, player.getGold());
        assertEquals(Collections.emptyList(), player.getHand());
    }

}