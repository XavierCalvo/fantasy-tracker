package com.fantasytracker.controller;

import com.fantasytracker.dto.PlayerRequest;
import com.fantasytracker.dto.PlayerResponse;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.Team;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TeamRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public List<PlayerResponse> list() {
        return playerRepository.findAll().stream().map(PlayerResponse::from).toList();
    }

    @GetMapping("/{id}")
    public PlayerResponse get(@PathVariable Long id) {
        return playerRepository.findById(id).map(PlayerResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Player " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        Team team = resolveTeam(request.teamId());
        Player player = new Player(request.name(), team, request.position(), request.externalId());
        Player saved = playerRepository.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponse.from(saved));
    }

    @PutMapping("/{id}")
    public PlayerResponse update(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Player " + id + " not found"));
        existing.setName(request.name());
        existing.setTeam(resolveTeam(request.teamId()));
        existing.setPosition(request.position());
        existing.setExternalId(request.externalId());
        return PlayerResponse.from(playerRepository.save(existing));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!playerRepository.existsById(id)) {
            throw new NoSuchElementException("Player " + id + " not found");
        }
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    private Team resolveTeam(Long teamId) {
        if (teamId == null) {
            return null;
        }
        return teamRepository.findById(teamId)
                .orElseThrow(() -> new NoSuchElementException("Team " + teamId + " not found"));
    }
}
