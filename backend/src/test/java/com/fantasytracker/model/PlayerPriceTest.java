package com.fantasytracker.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PlayerPriceTest {

    @Test
    void setPriceRejectsNegativeValues() {
        PlayerPrice price = new PlayerPrice();

        assertThrows(IllegalArgumentException.class, () -> price.setPrice(-1L));
    }

    @Test
    void constructorStoresPriceData() {
        Player player = new Player("Player");
        PlayerPrice price = new PlayerPrice(player, 100L, 5L, PlayerPriceTrendType.ACCELERATING_UP);

        assertEquals(player, price.getPlayer());
        assertEquals(100L, price.getPrice());
        assertEquals(5L, price.getTrendAmount());
        assertEquals(PlayerPriceTrendType.ACCELERATING_UP, price.getTrendType());
    }
}
