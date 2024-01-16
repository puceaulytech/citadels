package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.exceptions.DuplicatedDistrictException;

import org.junit.jupiter.api.Test;



import static org.junit.jupiter.api.Assertions.*;

class CityTest {

    @Test
    void addDistrict_shouldAddDistrictSuccessfully() throws DuplicatedDistrictException {

        City city = new City();
        District district = new District("TestDistrict", Category.MERVEILLE, 3);


        city.addDistrict(district);


        assertEquals(1, city.getSize());
        assertTrue(city.getDistricts().contains(district));
    }

    @Test
    void addDistrict_shouldThrowDuplicatedDistrictException() {

        City city = new City();
        District district = new District("TestDistrict", Category.MERVEILLE, 3);


        try {
            city.addDistrict(district);
            city.addDistrict(district); // Adding the same district again should throw an exception
            fail("DuplicatedDistrictException should have been thrown.");
        } catch (DuplicatedDistrictException e) {

            assertEquals(1, city.getSize());
            assertTrue(city.getDistricts().contains(district));
        }
    }

    @Test
    void getDistricts_shouldReturnImmutableSet() throws DuplicatedDistrictException {
        City city = new City();
        District district = new District("TestDistrict", Category.MERVEILLE, 3);
        city.addDistrict(district);


        assertThrows(UnsupportedOperationException.class, () ->
                city.getDistricts().add(new District("NewDistrict", Category.MILITAIRE, 4))
        );
    }

    @Test
    void iterator_shouldIterateOverDistricts() throws DuplicatedDistrictException {

        City city = new City();
        District district1 = new District("District1", Category.MERVEILLE, 3);
        District district2 = new District("District2", Category.NOBLE, 4);
        city.addDistrict(district1);
        city.addDistrict(district2);


        for (District district : city) {
            System.out.println(district);
        }
    }
}