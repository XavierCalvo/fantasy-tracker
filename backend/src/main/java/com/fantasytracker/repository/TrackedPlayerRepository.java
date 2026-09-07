package com.fantasytracker.repository;

import com.fantasytracker.model.TrackedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface TrackedPlayerRepository extends JpaRepository<TrackedPlayer, Long> {
    Optional<TrackedPlayer> findByPlayerId(Long playerId);

    @Query("select t from TrackedPlayer t join fetch t.player")
    List<TrackedPlayer> findAllWithPlayer();
}
