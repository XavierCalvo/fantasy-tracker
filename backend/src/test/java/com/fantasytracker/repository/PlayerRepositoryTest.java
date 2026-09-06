package com.fantasytracker.repository;

import com.fantasytracker.model.Player;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Test
    void savesAndLoadsPlayer() {
        Player player = new Player("Player", "Team", "MID", "ext-1");

        Player saved = playerRepository.save(player);

        assertNotNull(saved.getId());
        assertEquals("Player", playerRepository.findById(saved.getId()).orElseThrow().getName());
    }
}
