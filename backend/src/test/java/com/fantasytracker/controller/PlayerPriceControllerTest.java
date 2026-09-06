package com.fantasytracker.controller;

import com.fantasytracker.model.Player;
import com.fantasytracker.repository.PlayerPriceRepository;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TrackedPlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

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
}
