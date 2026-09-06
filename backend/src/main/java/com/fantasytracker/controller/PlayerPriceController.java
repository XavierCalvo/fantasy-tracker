package com.fantasytracker.controller;

import com.fantasytracker.dto.PlayerPriceRequest;
import com.fantasytracker.dto.PlayerPriceResponse;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPrice;
import com.fantasytracker.repository.PlayerPriceRepository;
import com.fantasytracker.repository.PlayerRepository;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

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
    public List<PlayerPriceResponse> list(@PathVariable Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new NoSuchElementException("Player " + playerId + " not found");
        }
        return playerPriceRepository.findByPlayerIdOrderByCapturedAtDesc(playerId).stream()
                .map(PlayerPriceResponse::from)
                .toList();
    }

    @PostMapping
    public ResponseEntity<PlayerPriceResponse> create(@PathVariable Long playerId, @Valid @RequestBody PlayerPriceRequest request) {
        Player player = playerRepository.findById(playerId)
                .orElseThrow(() -> new NoSuchElementException("Player " + playerId + " not found"));

        PlayerPrice price = new PlayerPrice(player, request.price(), request.trendAmount(), request.trendType());
        PlayerPrice saved = playerPriceRepository.save(price);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerPriceResponse.from(saved));
    }
}
