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
}
