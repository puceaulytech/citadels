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
    }
}
