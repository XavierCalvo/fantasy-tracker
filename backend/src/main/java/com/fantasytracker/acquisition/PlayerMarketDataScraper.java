package com.fantasytracker.acquisition;

/**
 * Fetches current market data for a player from an external source, identified by the player's
 * {@code externalId}. Implementations are responsible for the network call and delegating parsing
 * to a source-specific parser.
 */
public interface PlayerMarketDataScraper {

    /**
     * @param externalId the player's identifier on the external source (e.g. the futbolfantasy.com
     *                    URL slug).
     * @throws PlayerMarketDataException if the page cannot be fetched or parsed.
     */
    PlayerMarketPrice fetchLatestPrice(String externalId);
}
