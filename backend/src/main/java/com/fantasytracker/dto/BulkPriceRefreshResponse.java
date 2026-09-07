package com.fantasytracker.dto;

import java.util.List;

/**
 * Result of a bulk price refresh across every tracked player. Modeled as a per-player
 * success/failure list (rather than aborting on the first error) since a single scraping
 * failure (e.g. one player has no active market) should not prevent the rest from being
 * refreshed.
 */
public record BulkPriceRefreshResponse(List<Item> results) {

    public record Item(
            Long playerId,
            String playerName,
            boolean success,
            PlayerPriceResponse price,
            String error
    ) {
        public static Item success(Long playerId, String playerName, PlayerPriceResponse price) {
            return new Item(playerId, playerName, true, price, null);
        }

        public static Item failure(Long playerId, String playerName, String error) {
            return new Item(playerId, playerName, false, null, error);
        }
    }
}
