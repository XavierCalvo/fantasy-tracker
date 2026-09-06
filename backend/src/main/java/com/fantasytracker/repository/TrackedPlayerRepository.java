package com.fantasytracker.repository;

import com.fantasytracker.model.TrackedPlayer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TrackedPlayerRepository extends JpaRepository<TrackedPlayer, Long> { }
