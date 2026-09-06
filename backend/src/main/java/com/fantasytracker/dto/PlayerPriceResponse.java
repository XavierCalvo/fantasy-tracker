package com.fantasytracker.dto;

import com.fantasytracker.model.PlayerPrice;
import com.fantasytracker.model.PlayerPriceTrendType;

import java.time.ZonedDateTime;

public record PlayerPriceResponse(
        Long id,
        Long playerId,
        Long price,
        Long trendAmount,
        PlayerPriceTrendType trendType,
        ZonedDateTime capturedAt
) {
    public static PlayerPriceResponse from(PlayerPrice playerPrice) {
        return new PlayerPriceResponse(
                playerPrice.getId(),
                playerPrice.getPlayer().getId(),
                playerPrice.getPrice(),
                playerPrice.getTrendAmount(),
                playerPrice.getTrendType(),
                playerPrice.getCapturedAt()
        );
    }
}
