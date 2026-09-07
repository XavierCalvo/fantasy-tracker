package com.fantasytracker.controller;

import com.fantasytracker.model.Team;
import com.fantasytracker.repository.PlayerPriceRepository;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TeamRepository;
import com.fantasytracker.repository.TrackedPlayerRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
class PlayerControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private PlayerPriceRepository playerPriceRepository;

    @Autowired
    private TrackedPlayerRepository trackedPlayerRepository;

    @Autowired
    private TeamRepository teamRepository;

    @BeforeEach
    void cleanUp() {
        trackedPlayerRepository.deleteAll();
        playerPriceRepository.deleteAll();
        playerRepository.deleteAll();
        teamRepository.deleteAll();
    }

    @Test
    void createsAndRetrievesPlayer() throws Exception {
        Long teamId = teamRepository.save(new Team("Team A")).getId();

        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Player One\",\"teamId\":" + teamId + ",\"position\":\"MEDIO\",\"externalId\":\"ext-1\"}"))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").isNotEmpty())
                .andExpect(jsonPath("$.name").value("Player One"))
                .andExpect(jsonPath("$.teamName").value("Team A"))
                .andExpect(jsonPath("$.position").value("MEDIO"));

        mockMvc.perform(get("/api/players"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].name").value("Player One"));
    }

    @Test
    void getReturns404WhenPlayerMissing() throws Exception {
        mockMvc.perform(get("/api/players/999"))
                .andExpect(status().isNotFound())
                .andExpect(jsonPath("$.status").value(404));
    }

    @Test
    void createRejectsBlankName() throws Exception {
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"\"}"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.message").value("Validation failed"))
                .andExpect(jsonPath("$.details", not(empty())));
    }

    @Test
    void createRejectsUnknownTeamId() throws Exception {
        mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Player One\",\"teamId\":999}"))
                .andExpect(status().isNotFound());
    }

    @Test
    void updatesPlayer() throws Exception {
        Long id = createPlayer();
        Long teamId = teamRepository.save(new Team("Team B")).getId();

        mockMvc.perform(put("/api/players/" + id)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Updated Name\",\"teamId\":" + teamId + ",\"position\":\"DEFENSA\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Updated Name"))
                .andExpect(jsonPath("$.teamName").value("Team B"));
    }

    @Test
    void deletesPlayer() throws Exception {
        Long id = createPlayer();

        mockMvc.perform(delete("/api/players/" + id))
                .andExpect(status().isNoContent());

        mockMvc.perform(get("/api/players/" + id))
                .andExpect(status().isNotFound());
    }

    private Long createPlayer() throws Exception {
        String response = mockMvc.perform(post("/api/players")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"name\":\"Temp Player\"}"))
                .andExpect(status().isCreated())
                .andReturn().getResponse().getContentAsString();
        return Long.valueOf(response.replaceAll(".*\"id\":(\\d+).*", "$1"));
    }
}
