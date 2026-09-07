package com.fantasytracker.acquisition;

import com.fantasytracker.model.PlayerPriceTrendType;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.UncheckedIOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

class FutbolFantasyPriceParserTest {

    @Test
    void parsesAcceleratingUpwardTrend() {
        PlayerMarketPrice result = FutbolFantasyPriceParser.parse(loadFixture("alvaro-valles.html"));

        assertThat(result.price()).isEqualTo(44_766_796L);
        assertThat(result.trendAmount()).isEqualTo(1_618_275L);
        assertThat(result.trendType()).isEqualTo(PlayerPriceTrendType.ACCELERATING_STRONGLY_UP);
    }

    @Test
    void parsesStableUpwardTrend() {
        PlayerMarketPrice result = FutbolFantasyPriceParser.parse(loadFixture("david-soria.html"));

        assertThat(result.price()).isEqualTo(34_402_875L);
        assertThat(result.trendAmount()).isEqualTo(589_594L);
        assertThat(result.trendType()).isEqualTo(PlayerPriceTrendType.STABLE_UP);
    }

    @Test
    void parsesDownwardTrendWithNegativeAmount() {
        PlayerMarketPrice result = FutbolFantasyPriceParser.parse(loadFixture("falling-player.html"));

        assertThat(result.price()).isEqualTo(20_000_000L);
        assertThat(result.trendAmount()).isEqualTo(-500_000L);
        assertThat(result.trendType()).isEqualTo(PlayerPriceTrendType.ACCELERATING_DOWN);
    }

    @Test
    void throwsWhenValueWidgetIsMissing() {
        String html = loadFixture("missing-value.html");

        assertThatThrownBy(() -> FutbolFantasyPriceParser.parse(html))
                .isInstanceOf(PlayerMarketDataException.class)
                .hasMessageContaining("No se encontró un valor de mercado");
    }

    @Test
    void throwsWhenTrendIconIsUnrecognised() {
        String html = """
                <span class="valor-actual">Valor act: 1.000.000 (+1.000) \
                <i class="fas fa-question text-success"></i></span>""";

        assertThatThrownBy(() -> FutbolFantasyPriceParser.parse(html))
                .isInstanceOf(PlayerMarketDataException.class)
                .hasMessageContaining("Unrecognised trend icon");
    }

    private static String loadFixture(String name) {
        try {
            Path path = Path.of("src/test/resources/fixtures/futbolfantasy", name);
            return Files.readString(path, StandardCharsets.UTF_8);
        } catch (IOException e) {
            throw new UncheckedIOException(e);
        }
    }
}
