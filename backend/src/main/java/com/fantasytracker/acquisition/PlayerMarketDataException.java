package com.fantasytracker.acquisition;

/**
 * Raised whenever a player's market data cannot be fetched or parsed from an external source
 * (network failure, unexpected HTTP status, or a page structure that no longer matches the
 * expected markup). Mapped to HTTP 502 by {@link com.fantasytracker.web.GlobalExceptionHandler}.
 */
public class PlayerMarketDataException extends RuntimeException {

    public PlayerMarketDataException(String message) {
        super(message);
    }

    public PlayerMarketDataException(String message, Throwable cause) {
        super(message, cause);
    }
}
