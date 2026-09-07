package com.fantasytracker.acquisition;

import com.fantasytracker.model.PlayerPriceTrendType;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;

import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Parses the "current value" widget from a futbolfantasy.com player page.
 *
 * <p>The relevant markup looks like:
 * <pre>{@code
 * <span class="w-100 mx-auto valor-actual font-weight-bold">
 *   Valor act: 44.766.796 (<span class="analytics-up">+1.618.275</span>)
 *   <i class="fas fa-angle-double-up text-success hideQtip" data-tooltip="Acelera mucho"></i>
 *   ...
 * </span>
 * }</pre>
 *
 * <p>The trend icon/tooltip shown on the player page uses a short label (e.g. "Acelera mucho")
 * that does not by itself distinguish an upward move from a downward one (e.g. "Estable" is used
 * for both {@code fa-minus text-success} and {@code fa-minus text-danger}). The unambiguous key is
 * therefore the combination of the FontAwesome icon class and its colour class
 * ({@code text-success} = price is currently rising, {@code text-danger} = price is currently
 * falling), taken from futbolfantasy.com's own trend legend ("#leyendaAceleracion").
 */
public final class FutbolFantasyPriceParser {

    private static final String VALUE_SELECTOR = "span.valor-actual";
    private static final Pattern VALUE_PATTERN =
            Pattern.compile("Valor act:\\s*([\\d.]+)\\s*\\(([+-]?[\\d.]+)\\)");

    private static final Map<IconKey, PlayerPriceTrendType> TREND_BY_ICON = Map.ofEntries(
            Map.entry(new IconKey("fa-exclamation-triangle", "text-success"), PlayerPriceTrendType.INFLECTION_POSITIVE),
            Map.entry(new IconKey("fa-angle-double-up", "text-success"), PlayerPriceTrendType.ACCELERATING_STRONGLY_UP),
            Map.entry(new IconKey("fa-angle-up", "text-success"), PlayerPriceTrendType.ACCELERATING_UP),
            Map.entry(new IconKey("fa-minus", "text-success"), PlayerPriceTrendType.STABLE_UP),
            Map.entry(new IconKey("fa-angle-down", "text-success"), PlayerPriceTrendType.DECELERATING_UP),
            Map.entry(new IconKey("fa-angle-double-down", "text-success"), PlayerPriceTrendType.DECELERATING_STRONGLY_UP),
            Map.entry(new IconKey("fa-exclamation-triangle", "text-danger"), PlayerPriceTrendType.INFLECTION_NEGATIVE),
            Map.entry(new IconKey("fa-angle-double-up", "text-danger"), PlayerPriceTrendType.DECELERATING_STRONGLY_DOWN),
            Map.entry(new IconKey("fa-angle-up", "text-danger"), PlayerPriceTrendType.DECELERATING_DOWN),
            Map.entry(new IconKey("fa-minus", "text-danger"), PlayerPriceTrendType.STABLE_DOWN),
            Map.entry(new IconKey("fa-angle-down", "text-danger"), PlayerPriceTrendType.ACCELERATING_DOWN),
            Map.entry(new IconKey("fa-angle-double-down", "text-danger"), PlayerPriceTrendType.ACCELERATING_STRONGLY_DOWN)
    );

    private FutbolFantasyPriceParser() {
    }

    /**
     * @param html raw HTML of (or containing) a futbolfantasy.com player page.
     * @return the parsed current price, trend amount and trend type.
     * @throws PlayerMarketDataException if the expected markup is missing or unrecognised.
     */
    public static PlayerMarketPrice parse(String html) {
        Document document = Jsoup.parse(html);
        Element valueElement = document.selectFirst(VALUE_SELECTOR);
        if (valueElement == null) {
            throw new PlayerMarketDataException(
                    "Could not find the '" + VALUE_SELECTOR + "' element on the player page");
        }

        Matcher matcher = VALUE_PATTERN.matcher(valueElement.text());
        if (!matcher.find()) {
            throw new PlayerMarketDataException(
                    "Could not parse the current value text: '" + valueElement.text() + "'");
        }

        Long price = parseUnsignedAmount(matcher.group(1));
        Long trendAmount = parseSignedAmount(matcher.group(2));
        PlayerPriceTrendType trendType = parseTrendType(valueElement);

        return new PlayerMarketPrice(price, trendAmount, trendType);
    }

    private static PlayerPriceTrendType parseTrendType(Element valueElement) {
        Element icon = valueElement.selectFirst("i.fas");
        if (icon == null) {
            throw new PlayerMarketDataException("Could not find the trend icon on the player page");
        }

        String iconClass = TREND_BY_ICON.keySet().stream()
                .map(IconKey::icon)
                .filter(icon::hasClass)
                .findFirst()
                .orElseThrow(() -> new PlayerMarketDataException(
                        "Unrecognised trend icon classes: '" + icon.className() + "'"));

        String colorClass = icon.hasClass("text-success") ? "text-success"
                : icon.hasClass("text-danger") ? "text-danger"
                : null;
        if (colorClass == null) {
            throw new PlayerMarketDataException(
                    "Unrecognised trend icon colour classes: '" + icon.className() + "'");
        }

        PlayerPriceTrendType trendType = TREND_BY_ICON.get(new IconKey(iconClass, colorClass));
        if (trendType == null) {
            throw new PlayerMarketDataException(
                    "Unmapped trend icon/colour combination: '" + iconClass + " " + colorClass + "'");
        }
        return trendType;
    }

    private static Long parseUnsignedAmount(String rawAmount) {
        return Long.parseLong(rawAmount.replace(".", ""));
    }

    private static Long parseSignedAmount(String rawAmount) {
        String normalised = rawAmount.replace(".", "");
        if (normalised.startsWith("+")) {
            normalised = normalised.substring(1);
        }
        return Long.parseLong(normalised);
    }

    private record IconKey(String icon, String color) {
    }
}
