package com.fantasytracker.model;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class TrackedPlayerTest {

    @Test
    void setPlayerRejectsNull() {
        TrackedPlayer trackedPlayer = new TrackedPlayer();

        assertThrows(IllegalArgumentException.class, () -> trackedPlayer.setPlayer(null));
    }

    @Test
    void setClauseRejectsNegativeValues() {
        TrackedPlayer trackedPlayer = new TrackedPlayer(new Player("Player"));

        assertThrows(IllegalArgumentException.class, () -> trackedPlayer.setClause(new BigDecimal("-1")));
    }

    @Test
    void settersUpdateTrackingFields() {
        Player player = new Player("Player");
        TrackedPlayer trackedPlayer = new TrackedPlayer(player);

        trackedPlayer.setStatus(TrackedPlayerStatus.OWNED);
        trackedPlayer.setClause(new BigDecimal("1000000"));
        trackedPlayer.setClauseReleaseDate(LocalDate.of(2026, 9, 1));
        trackedPlayer.setNotes("Test note");

        assertEquals(player, trackedPlayer.getPlayer());
        assertEquals(TrackedPlayerStatus.OWNED, trackedPlayer.getStatus());
        assertEquals(new BigDecimal("1000000"), trackedPlayer.getClause());
        assertEquals(LocalDate.of(2026, 9, 1), trackedPlayer.getClauseReleaseDate());
        assertEquals("Test note", trackedPlayer.getNotes());
    }
}
