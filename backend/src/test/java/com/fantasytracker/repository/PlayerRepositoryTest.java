package com.fantasytracker.repository;

import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPosition;
import com.fantasytracker.model.Team;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @Test
    void savesAndLoadsPlayer() {
        Team team = teamRepository.save(new Team("Team"));
        Player player = new Player("Player", team, PlayerPosition.MEDIO, "ext-1");

        Player saved = playerRepository.save(player);

        assertNotNull(saved.getId());
        assertEquals("Player", playerRepository.findById(saved.getId()).orElseThrow().getName());
    }
}
