package com.fantasytracker.controller;

import com.fantasytracker.model.Player;
import com.fantasytracker.repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/players")
public class PlayerController {
    private final PlayerRepository playerRepository;

    public PlayerController(PlayerRepository playerRepository) { this.playerRepository = playerRepository; }

    @GetMapping
    public List<Player> list() { return playerRepository.findAll(); }

    @GetMapping("/{id}")
    public ResponseEntity<Player> get(@PathVariable Long id) {
        return playerRepository.findById(id).map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public Player create(@RequestBody Player p) { return playerRepository.save(p); }

    @PutMapping("/{id}")
    public ResponseEntity<Player> update(@PathVariable Long id, @RequestBody Player p) {
        return playerRepository.findById(id).map(existing -> {
            existing.setName(p.getName());
            existing.setTeam(p.getTeam());
            existing.setPosition(p.getPosition());
            existing.setExternalId(p.getExternalId());
            return ResponseEntity.ok(playerRepository.save(existing));
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!playerRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        playerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}
