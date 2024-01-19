package com.github.the10xdevs.citadels.models;

import com.github.the10xdevs.citadels.logging.ConsoleLogger;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class CategoryTest {

    @Test
    void testToString() {
        assertEquals("Noble", Category.NOBLE.toString());
        assertEquals("Religieux", Category.RELIGIEUX.toString());
        assertEquals("Marchand", Category.MARCHAND.toString());
        assertEquals("Militaire", Category.MILITAIRE.toString());
        assertEquals("Merveille", Category.MERVEILLE.toString());
    }

    @Test
    void testGetANSIColorCode() {
        assertEquals(ConsoleLogger.ANSI_YELLOW, Category.NOBLE.getANSIColorCode());
        assertEquals(ConsoleLogger.ANSI_BLUE, Category.RELIGIEUX.getANSIColorCode());
        assertEquals(ConsoleLogger.ANSI_GREEN, Category.MARCHAND.getANSIColorCode());
        assertEquals(ConsoleLogger.ANSI_RED, Category.MILITAIRE.getANSIColorCode());
        assertEquals(ConsoleLogger.ANSI_PURPLE, Category.MERVEILLE.getANSIColorCode());
    }
}