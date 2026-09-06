package com.fantasytracker.repository;

import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPrice;
import com.fantasytracker.model.PlayerPriceTrendType;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class PlayerPriceRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerPriceRepository playerPriceRepository;

    @Test
    void findsPricesInReverseCapturedOrder() {
        Player player = playerRepository.save(new Player("Player"));
        PlayerPrice first = playerPriceRepository.save(new PlayerPrice(player, 100L, 1L, PlayerPriceTrendType.STABLE_UP));
        PlayerPrice second = playerPriceRepository.save(new PlayerPrice(player, 110L, 10L, PlayerPriceTrendType.ACCELERATING_UP));

        List<PlayerPrice> prices = playerPriceRepository.findByPlayerIdOrderByCapturedAtDesc(player.getId());

        assertEquals(List.of(second, first), prices);
    }
}
