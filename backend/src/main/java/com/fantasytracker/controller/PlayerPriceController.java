package com.fantasytracker.controller;

import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPrice;
import com.fantasytracker.repository.PlayerPriceRepository;
import com.fantasytracker.repository.PlayerRepository;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/players/{playerId}/prices")
public class PlayerPriceController {

    private final PlayerPriceRepository playerPriceRepository;
    private final PlayerRepository playerRepository;

    public PlayerPriceController(PlayerPriceRepository playerPriceRepository, PlayerRepository playerRepository) {
        this.playerPriceRepository = playerPriceRepository;
        this.playerRepository = playerRepository;
    }

    @GetMapping
    public ResponseEntity<List<PlayerPrice>> list(@PathVariable Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(playerPriceRepository.findByPlayerIdOrderByCapturedAtDesc(playerId));
    }

    @PostMapping
    public ResponseEntity<PlayerPrice> create(@PathVariable Long playerId, @RequestBody PlayerPrice price) {
        return playerRepository.findById(playerId).map(player -> {
            price.setId(null);
            price.setPlayer(player);
            PlayerPrice saved = playerPriceRepository.save(price);
            return ResponseEntity.ok(saved);
        }).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
