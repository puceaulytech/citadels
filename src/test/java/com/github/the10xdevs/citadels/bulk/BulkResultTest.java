package com.github.the10xdevs.citadels.bulk;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BulkResultTest {
    BulkResult bulkResult;

    @BeforeEach
    void setup() {
        bulkResult = new BulkResult();
    }

    @Test
    void testInitialValues() {
        assertEquals(0, bulkResult.getWins());
        assertEquals(0, bulkResult.getLoses());
        assertEquals(0, bulkResult.getAverageScore());
    }

    @Test
    void testAddWin() {
        bulkResult.addWin();
        assertEquals(1, bulkResult.getWins());
    }

    @Test
    void testAddMultipleWins() {
        bulkResult.addWin(3);
        assertEquals(3, bulkResult.getWins());
    }

    @Test
    void testAddLoss() {
        bulkResult.addLoss();
        assertEquals(1, bulkResult.getLoses());
    }

    @Test
    void testAddMultipleLosses() {
        bulkResult.addLoss(2);
        assertEquals(2, bulkResult.getLoses());
    }

    @Test
    void testIncrementAverageScore() {
        bulkResult.incrementAverageScore(5.0);
        assertEquals(5.0, bulkResult.getAverageScore());
    }

    @Test
    void testSetAverageScore() {
        bulkResult.setAverageScore(7.5);
        assertEquals(7.5, bulkResult.getAverageScore());
    }

    @Test
    void testGetWinPercentage() {
        bulkResult.addWin(3);
        assertEquals(75.0, bulkResult.getWinPercentage(4)); // 3 wins out of 4 iterations = 75%
    }
}