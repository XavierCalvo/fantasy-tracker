package com.fantasytracker.acquisition;

import org.springframework.stereotype.Component;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * {@link PlayerMarketDataScraper} implementation for futbolfantasy.com.
 *
 * <p>The player profile page ({@code /jugadores/<slug>}) does NOT render the current value
 * widget server-side: it shows a loading placeholder and fetches it client-side via jQuery from
 * {@code /analytics/laliga-fantasy/mercado/detalle/<numericId>?perfil=1}, keyed by the player's
 * internal numeric id (embedded in the profile page's inline script, not the slug). Fetching the
 * value therefore takes two requests: first the profile page to discover the numeric id, then the
 * "mercado/detalle" fragment that actually contains {@code span.valor-actual}.
 */
@Component
public class FutbolFantasyPlayerScraper implements PlayerMarketDataScraper {

    private static final String PROFILE_URL_TEMPLATE = "https://www.futbolfantasy.com/jugadores/%s";
    private static final String MARKET_DETAIL_URL_TEMPLATE =
            "https://www.futbolfantasy.com/analytics/laliga-fantasy/mercado/detalle/%s?perfil=1";
    private static final Pattern MARKET_DETAIL_ID_PATTERN = Pattern.compile("mercado/detalle/(\\d+)");
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
        String profileHtml = fetchHtml(PROFILE_URL_TEMPLATE.formatted(externalId), externalId);
        String marketId = extractMarketDetailId(profileHtml, externalId);
        String marketHtml = fetchHtml(MARKET_DETAIL_URL_TEMPLATE.formatted(marketId), externalId);
        return FutbolFantasyPriceParser.parse(marketHtml);
    }

    private static String extractMarketDetailId(String profileHtml, String externalId) {
        Matcher matcher = MARKET_DETAIL_ID_PATTERN.matcher(profileHtml);
        if (!matcher.find()) {
            throw new PlayerMarketDataException(
                    "Could not find the market detail id on the profile page for player '" + externalId + "'");
        }
        return matcher.group(1);
    }

    private String fetchHtml(String url, String externalId) {
        URI uri = URI.create(url);
        HttpRequest request = HttpRequest.newBuilder(uri)
                .timeout(REQUEST_TIMEOUT)
                .header("User-Agent", USER_AGENT)
                .header("X-Requested-With", "XMLHttpRequest")
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
