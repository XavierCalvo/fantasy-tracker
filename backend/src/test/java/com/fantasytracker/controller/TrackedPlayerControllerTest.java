package com.fantasytracker.controller;

import com.fantasytracker.acquisition.PlayerMarketDataException;
import com.fantasytracker.acquisition.PlayerMarketDataScraper;
import com.fantasytracker.acquisition.PlayerMarketPrice;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPrice;
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

import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.hasSize;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class TrackedPlayerControllerTest {

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
    void createsAndRetrievesTracking() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OWNED\",\"clause\":1000000,\"notes\":\"Watch closely\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("OWNED"))
                .andExpect(jsonPath("$.playerId").value(playerId));

        mockMvc.perform(get("/api/players/" + playerId + "/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.notes").value("Watch closely"));
    }

    @Test
    void createDefaultsStatusWhenNotProvided() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.status").value("WATCHING"));
    }

    @Test
    void createReturns404WhenPlayerMissing() throws Exception {
        mockMvc.perform(post("/api/players/999999/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void createRejectsNegativeClause() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"clause\":-500}"))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updatesAndDeletesTracking() throws Exception {
        String response = mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        Long trackingId = Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));

        mockMvc.perform(put("/api/tracking/" + trackingId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"DISCARDED\",\"notes\":\"No longer interesting\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("DISCARDED"))
                .andExpect(jsonPath("$.notes").value("No longer interesting"));

        mockMvc.perform(delete("/api/tracking/" + trackingId))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/players/" + playerId + "/tracking"))
                .andExpect(status().isNotFound());
    }

    @Test
    void listsAllTrackedPlayersWithPlayerInfo() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"status\":\"OWNED\",\"clauseReleaseDate\":\"2026-06-30\"}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].playerId").value(playerId))
                .andExpect(jsonPath("$[0].playerName").value("Player"))
                .andExpect(jsonPath("$[0].status").value("OWNED"))
                .andExpect(jsonPath("$[0].clauseReleaseDate").value("2026-06-30"));
    }

    @Test
    void listIncludesLatestPriceWhenAvailable() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());

        Player player = playerRepository.findById(playerId).orElseThrow();
        playerPriceRepository.save(new PlayerPrice(player, 1_000_000L, 10_000L, PlayerPriceTrendType.STABLE_UP));
        playerPriceRepository.save(new PlayerPrice(player, 1_050_000L, 50_000L, PlayerPriceTrendType.ACCELERATING_UP));

        mockMvc.perform(get("/api/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latestPrice").value(1_050_000))
                .andExpect(jsonPath("$[0].latestTrendAmount").value(50_000))
                .andExpect(jsonPath("$[0].latestTrendType").value("ACCELERATING_UP"))
                .andExpect(jsonPath("$[0].latestPriceCapturedAt").exists());
    }

    @Test
    void listOmitsLatestPriceWhenNoneRecorded() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/tracking"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].latestPrice").doesNotExist());
    }

    @Test
    void refreshAllRefreshesEveryTrackedPlayerAndReportsPerPlayerResults() throws Exception {
        mockMvc.perform(post("/api/players/" + playerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());

        Player ok = playerRepository.findById(playerId).orElseThrow();
        ok.setExternalId("alvaro-valles");
        playerRepository.save(ok);

        Long secondPlayerId = playerRepository.save(new Player("Second Player")).getId();
        mockMvc.perform(post("/api/players/" + secondPlayerId + "/tracking")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{}"))
                .andExpect(status().isCreated());
        Player failing = playerRepository.findById(secondPlayerId).orElseThrow();
        failing.setExternalId("no-market-player");
        playerRepository.save(failing);

        when(playerMarketDataScraper.fetchLatestPrice("alvaro-valles"))
                .thenReturn(new PlayerMarketPrice(44_766_796L, 1_618_275L, PlayerPriceTrendType.ACCELERATING_STRONGLY_UP));
        when(playerMarketDataScraper.fetchLatestPrice("no-market-player"))
                .thenThrow(new PlayerMarketDataException("No se encontró un valor de mercado para este jugador"));

        mockMvc.perform(post("/api/tracking/prices/refresh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(2)))
                .andExpect(jsonPath("$.results[?(@.playerId == " + playerId + ")].success").value(hasItem(true)))
                .andExpect(jsonPath("$.results[?(@.playerId == " + secondPlayerId + ")].success").value(hasItem(false)))
                .andExpect(jsonPath("$.results[?(@.playerId == " + secondPlayerId + ")].error")
                        .value(hasItem("No se encontró un valor de mercado para este jugador")));

        mockMvc.perform(get("/api/players/" + playerId + "/prices"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)));
    }

    @Test
    void refreshAllReturnsEmptyResultsWhenNoPlayersTracked() throws Exception {
        mockMvc.perform(post("/api/tracking/prices/refresh"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.results", hasSize(0)));
    }
}
