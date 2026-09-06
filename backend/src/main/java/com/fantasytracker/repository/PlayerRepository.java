package com.fantasytracker.repository;

import com.fantasytracker.model.Player;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerRepository extends JpaRepository<Player, Long> { }
