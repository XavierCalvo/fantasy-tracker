package com.fantasytracker.repository;

import com.fantasytracker.model.PlayerPrice;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface PlayerPriceRepository extends JpaRepository<PlayerPrice, Long> {
    List<PlayerPrice> findByPlayerIdOrderByCapturedAtDesc(Long playerId);

    List<PlayerPrice> findByPlayerIdInOrderByCapturedAtDesc(List<Long> playerIds);
}
