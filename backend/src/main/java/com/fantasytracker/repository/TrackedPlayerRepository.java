package com.fantasytracker.repository;

import com.fantasytracker.model.TrackedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TrackedPlayerRepository extends JpaRepository<TrackedPlayer, Long> {
    Optional<TrackedPlayer> findByPlayerId(Long playerId);
}
