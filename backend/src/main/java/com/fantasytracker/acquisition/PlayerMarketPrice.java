package com.fantasytracker.acquisition;

import com.fantasytracker.model.PlayerPriceTrendType;

/**
 * Market data for a single player, scraped from an external source (currently futbolfantasy.com).
 *
 * @param price       current market value, in euros.
 * @param trendAmount signed change amount associated with the current trend (positive for
 *                    upward movement, negative for downward movement).
 * @param trendType   qualitative trend classification, derived from the source's icon/tooltip.
 */
public record PlayerMarketPrice(Long price, Long trendAmount, PlayerPriceTrendType trendType) {
}
