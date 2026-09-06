package com.fantasytracker.repository;

import com.fantasytracker.model.Player;
import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.model.TrackedPlayerStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@DataJpaTest
class TrackedPlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private TrackedPlayerRepository trackedPlayerRepository;

    @Test
    void savesTrackedPlayerWithDefaultStatus() {
        Player player = playerRepository.save(new Player("Player"));
        TrackedPlayer trackedPlayer = trackedPlayerRepository.save(new TrackedPlayer(player));

        assertNotNull(trackedPlayer.getId());
        assertEquals(TrackedPlayerStatus.WATCHING, trackedPlayerRepository.findById(trackedPlayer.getId()).orElseThrow().getStatus());
    }
}
