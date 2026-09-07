package com.fantasytracker.controller;

import com.fantasytracker.dto.TrackedPlayerListItemResponse;
import com.fantasytracker.dto.TrackedPlayerRequest;
import com.fantasytracker.dto.TrackedPlayerResponse;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TrackedPlayerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
public class TrackedPlayerController {

    private final TrackedPlayerRepository trackedPlayerRepository;
    private final PlayerRepository playerRepository;

    public TrackedPlayerController(TrackedPlayerRepository trackedPlayerRepository, PlayerRepository playerRepository) {
        this.trackedPlayerRepository = trackedPlayerRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/api/players/{playerId}/tracking")
    public TrackedPlayerResponse getByPlayer(@PathVariable Long playerId) {
        return trackedPlayerRepository.findByPlayerId(playerId)
                .map(TrackedPlayerResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Tracking for player " + playerId + " not found"));
    }

    @GetMapping("/api/tracking")
    public List<TrackedPlayerListItemResponse> listAll() {
        return trackedPlayerRepository.findAllWithPlayer().stream()
                .map(TrackedPlayerListItemResponse::from)
                .toList();
    }

    @PostMapping("/api/players/{playerId}/tracking")
    public ResponseEntity<TrackedPlayerResponse> create(@PathVariable Long playerId, @Valid @RequestBody TrackedPlayerRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player " + playerId + " not found"));

        TrackedPlayer entity = new TrackedPlayer(player);
        if (request.status() != null) {
            entity.setStatus(request.status());
        }
        entity.setClause(request.clause());
        entity.setClauseReleaseDate(request.clauseReleaseDate());
        entity.setNotes(request.notes());
        TrackedPlayer saved = trackedPlayerRepository.save(entity);
        return ResponseEntity.status(HttpStatus.CREATED).body(TrackedPlayerResponse.from(saved));
    }

    @PutMapping("/api/tracking/{id}")
    public TrackedPlayerResponse update(@PathVariable Long id, @Valid @RequestBody TrackedPlayerRequest request) {
        TrackedPlayer existing = trackedPlayerRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Tracking " + id + " not found"));

        if (request.status() != null) {
            existing.setStatus(request.status());
        }
        existing.setClause(request.clause());
        existing.setClauseReleaseDate(request.clauseReleaseDate());
        existing.setNotes(request.notes());
        return TrackedPlayerResponse.from(trackedPlayerRepository.save(existing));
    }

    @DeleteMapping("/api/tracking/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!trackedPlayerRepository.existsById(id)) {
            throw new NoSuchElementException("Tracking " + id + " not found");
        }
        trackedPlayerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
