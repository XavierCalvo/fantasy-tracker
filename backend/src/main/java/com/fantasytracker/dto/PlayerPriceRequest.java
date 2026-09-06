package com.fantasytracker.dto;

import com.fantasytracker.model.PlayerPriceTrendType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record PlayerPriceRequest(
        @NotNull(message = "Price is required")
        @PositiveOrZero(message = "Price must be non-negative")
        Long price,

        Long trendAmount,

        PlayerPriceTrendType trendType
) {
}
