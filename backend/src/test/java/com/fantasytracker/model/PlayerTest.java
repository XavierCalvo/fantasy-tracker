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
        Team team = new Team("Team");
        Player player = new Player("Player", team, PlayerPosition.MEDIO, "ext-1");

        assertEquals("Player", player.getName());
        assertEquals(team, player.getTeam());
        assertEquals(PlayerPosition.MEDIO, player.getPosition());
        assertEquals("ext-1", player.getExternalId());
    }
}
