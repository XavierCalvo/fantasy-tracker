package com.fantasytracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerTest {

    @Test
    void setNameRejectsBlankValues() {
        Player player = new Player();

        assertThrows(IllegalArgumentException.class, () -> player.setName(""));
        assertThrows(IllegalArgumentException.class, () -> player.setName("   "));
    }

    @Test
    void constructorStoresBasicFields() {
        Player player = new Player("Player", "Team", "MID", "ext-1");

        assertEquals("Player", player.getName());
        assertEquals("Team", player.getTeam());
        assertEquals("MID", player.getPosition());
        assertEquals("ext-1", player.getExternalId());
    }
}
