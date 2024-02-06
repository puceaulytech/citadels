package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void addDistrict() throws DuplicatedDistrictException {

        City city = new City();
        District district = new District("TestDistrict", Category.MERVEILLE, 3);


        city.addDistrict(district);


        assertEquals(1, city.getSize());
        assertTrue(city.getDistricts().contains(district));
    }

    @Test
    void addDistrict_shouldThrowDuplicatedDistrictException() throws DuplicatedDistrictException {

        City city = new City();
        District district = new District("TestDistrict", Category.MERVEILLE, 3);


        city.addDistrict(district);
        assertThrows(DuplicatedDistrictException.class, () -> city.addDistrict(district)); // Adding the same district again should throw an exception
    }

    @Test
    void iterator() throws DuplicatedDistrictException {
        City city = new City();
        District district1 = new District("District1", Category.MERVEILLE, 3);
        District district2 = new District("District2", Category.NOBLE, 4);
        city.addDistrict(district1);
        city.addDistrict(district2);

        int iterationCount = 0;
        for (District ignored : city) {
            iterationCount++;
        }

        assertEquals(2, iterationCount);
    }
}