package com.fantasytracker.controller;

import com.fantasytracker.dto.PlayerRequest;
import com.fantasytracker.dto.PlayerResponse;
import com.fantasytracker.model.Player;
import com.fantasytracker.repository.PlayerRepository;
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

    public PlayerController(PlayerRepository playerRepository) { this.playerRepository = playerRepository; }

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
        Player player = new Player(request.name(), request.team(), request.position(), request.externalId());
        Player saved = playerRepository.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponse.from(saved));
    }

    @PutMapping("/{id}")
    public PlayerResponse update(@PathVariable Long id, @Valid @RequestBody PlayerRequest request) {
        Player existing = playerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Player " + id + " not found"));
        existing.setName(request.name());
        existing.setTeam(request.team());
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
}
