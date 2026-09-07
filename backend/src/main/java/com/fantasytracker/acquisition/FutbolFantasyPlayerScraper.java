package com.fantasytracker.acquisition;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;

/**
 * {@link PlayerMarketDataScraper} implementation for futbolfantasy.com.
 *
 * <p>Player pages are plain server-rendered HTML (no client-side rendering is required to see the
 * current value widget), so a single HTTP GET plus {@link FutbolFantasyPriceParser} is enough;
 * no headless browser is needed.
 */
@Component
public class FutbolFantasyPlayerScraper implements PlayerMarketDataScraper {

    private static final String PLAYER_URL_TEMPLATE = "https://www.futbolfantasy.com/jugadores/%s";
    private static final String USER_AGENT =
            "Mozilla/5.0 (compatible; FantasyTrackerBot/1.0; +https://github.com/XavierCalvo/fantasy-tracker)";
    private static final Duration REQUEST_TIMEOUT = Duration.ofSeconds(10);

    private final HttpClient httpClient;

    public FutbolFantasyPlayerScraper() {
        this.httpClient = HttpClient.newBuilder()
                .connectTimeout(REQUEST_TIMEOUT)
                .followRedirects(HttpClient.Redirect.NORMAL)
                .build();
    }

    @Override
    public PlayerMarketPrice fetchLatestPrice(String externalId) {
        String html = fetchHtml(externalId);
        return FutbolFantasyPriceParser.parse(html);
    }

    private String fetchHtml(String externalId) {
        URI uri = URI.create(PLAYER_URL_TEMPLATE.formatted(externalId));
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("User-Agent", USER_AGENT)
                .GET()
                .build();

        HttpResponse<String> response;
        try {
            response = httpClient.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException e) {
            throw new PlayerMarketDataException("Failed to reach futbolfantasy.com for player '" + externalId + "'", e);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new PlayerMarketDataException("Interrupted while fetching player '" + externalId + "'", e);
        }

        if (response.statusCode() != 200) {
            throw new PlayerMarketDataException(
                    "futbolfantasy.com returned HTTP " + response.statusCode() + " for player '" + externalId + "'");
        }
        return response.body();
    }
}
