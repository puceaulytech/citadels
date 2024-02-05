package com.github.the10xdevs.citadels.models;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

class DistrictTest {
    @SuppressWarnings({"EqualsWithItself", "AssertBetweenInconvertibleTypes"})
    @Test
    void equals() {
        District romain = new District("Baraque de Romain", Category.MERVEILLE, 10);
        District logan = new District("Baraque de Logan", Category.MILITAIRE, 10);
        District vahan = new District("Baraque de Vahan", Category.MILITAIRE, 10);
        District otherRomain = new District("Baraque de Romain", Category.MERVEILLE, 10);

        assertEquals(romain, romain);
        assertEquals(romain, otherRomain);
        assertNotEquals(romain, logan);
        assertNotEquals(vahan, logan);
        assertNotEquals(4, vahan);
        assertNotEquals(romain, null);
        assertNotEquals(romain, "Baraque de Romain");

        // Test hashCode
        assertEquals(romain.hashCode(), otherRomain.hashCode());
        assertNotEquals(romain.hashCode(), logan.hashCode());
    }

    @Test
    void toStringFormat() {
        District district = new District("Baraque de Romain", Category.MERVEILLE, 10);
        String expectedString = "Baraque de Romain, Merveille, prix: 10, score : 10";
        assertEquals(expectedString, district.toString());
    }

    @Test
    void getCategory() {
        District district = new District("Test District", Category.MILITAIRE, 5, 8);
        assertEquals(Category.MILITAIRE, district.getCategory());
    }

    @Test
    void getCost() {
        District district = new District("Test District", Category.MILITAIRE, 5, 8);
        assertEquals(5, district.getCost());
    }

    @Test
    void getScore() {
        District district = new District("Test District", Category.MILITAIRE, 5, 8);
        assertEquals(8, district.getScore());
    }

    @Test
    void getName() {
        District district = new District("Test District", Category.MILITAIRE, 5, 8);
        assertEquals("Test District", district.getName());
    }


}


