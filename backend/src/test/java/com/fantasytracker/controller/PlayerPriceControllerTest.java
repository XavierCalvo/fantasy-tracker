package com.fantasytracker.controller;

import com.fantasytracker.acquisition.PlayerMarketDataException;
import com.fantasytracker.acquisition.PlayerMarketDataScraper;
import com.fantasytracker.acquisition.PlayerMarketPrice;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPriceTrendType;
import com.fantasytracker.repository.PlayerPriceRepository;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TrackedPlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.hamcrest.Matchers.hasSize;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerPriceControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerPriceRepository playerPriceRepository;

    @Autowired
    private TrackedPlayerRepository trackedPlayerRepository;

    @MockBean
    private PlayerMarketDataScraper playerMarketDataScraper;

    private Long playerId;

    @BeforeEach
    void setUp() {
        trackedPlayerRepository.deleteAll();
        playerPriceRepository.deleteAll();
        playerRepository.deleteAll();
        playerId = playerRepository.save(new Player("Player")).getId();
    }

    @Test
    void recordsAndListsPriceHistory() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":50000000,\"trendAmount\":100000,\"trendType\":\"STABLE_UP\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(50000000))
                .andExpect(jsonPath("$.playerId").value(playerId));

        mockMvc.perform(get("/api/players/" + playerId + "/prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void createReturns404WhenPlayerMissing() throws Exception {
        mockMvc.perform(post("/api/players/999999/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":1000}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRejectsNegativePrice() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/prices")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"price\":-10}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"));
    }

    @Test
    void listReturns404WhenPlayerMissing() throws Exception {
        mockMvc.perform(get("/api/players/999999/prices"))
                .andExpect(status().isNotFound());
    }

    @Test
    void refreshScrapesAndRecordsANewPrice() throws Exception {
        Player player = playerRepository.findById(playerId).orElseThrow();
        player.setExternalId("alvaro-valles");
        playerRepository.save(player);

        when(playerMarketDataScraper.fetchLatestPrice("alvaro-valles"))
                .thenReturn(new PlayerMarketPrice(44_766_796L, 1_618_275L, PlayerPriceTrendType.ACCELERATING_STRONGLY_UP));

        mockMvc.perform(post("/api/players/" + playerId + "/prices/refresh"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.price").value(44_766_796))
                .andExpect(jsonPath("$.trendAmount").value(1_618_275))
                .andExpect(jsonPath("$.trendType").value("ACCELERATING_STRONGLY_UP"));

        mockMvc.perform(get("/api/players/" + playerId + "/prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void refreshReturns400WhenPlayerHasNoExternalId() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/prices/refresh"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Player " + playerId + " has no external id configured"));
    }

    @Test
    void refreshReturns404WhenPlayerMissing() throws Exception {
        mockMvc.perform(post("/api/players/999999/prices/refresh"))
                .andExpect(status().isNotFound());
    }

    @Test
    void refreshReturns502WhenScrapingFails() throws Exception {
        Player player = playerRepository.findById(playerId).orElseThrow();
        player.setExternalId("alvaro-valles");
        playerRepository.save(player);

        when(playerMarketDataScraper.fetchLatestPrice(any()))
                .thenThrow(new PlayerMarketDataException("futbolfantasy.com is unreachable"));

        mockMvc.perform(post("/api/players/" + playerId + "/prices/refresh"))
                .andExpect(status().isBadGateway())
                .andExpect(jsonPath("$.message").value("futbolfantasy.com is unreachable"));
    }
}
