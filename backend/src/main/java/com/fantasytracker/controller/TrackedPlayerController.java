package com.fantasytracker.controller;

import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TrackedPlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class TrackedPlayerController {

    private final TrackedPlayerRepository trackedPlayerRepository;
    private final PlayerRepository playerRepository;

    public TrackedPlayerController(TrackedPlayerRepository trackedPlayerRepository, PlayerRepository playerRepository) {
        this.trackedPlayerRepository = trackedPlayerRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping("/api/players/{playerId}/tracking")
    public ResponseEntity<TrackedPlayer> getByPlayer(@PathVariable Long playerId) {
        return trackedPlayerRepository.findByPlayerId(playerId)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/api/players/{playerId}/tracking")
    public ResponseEntity<TrackedPlayer> create(@PathVariable Long playerId, @RequestBody TrackedPlayer tracking) {
        return playerRepository.findById(playerId).map(player -> {
            TrackedPlayer entity = new TrackedPlayer(player);
            if (tracking.getStatus() != null) {
                entity.setStatus(tracking.getStatus());
            }
            entity.setClause(tracking.getClause());
            entity.setClauseReleaseDate(tracking.getClauseReleaseDate());
            entity.setNotes(tracking.getNotes());
            return ResponseEntity.ok(trackedPlayerRepository.save(entity));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/api/tracking/{id}")
    public ResponseEntity<TrackedPlayer> update(@PathVariable Long id, @RequestBody TrackedPlayer tracking) {
        return trackedPlayerRepository.findById(id).map(existing -> {
            if (tracking.getStatus() != null) {
                existing.setStatus(tracking.getStatus());
            }
            existing.setClause(tracking.getClause());
            existing.setClauseReleaseDate(tracking.getClauseReleaseDate());
            existing.setNotes(tracking.getNotes());
            return ResponseEntity.ok(trackedPlayerRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/api/tracking/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!trackedPlayerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        trackedPlayerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
