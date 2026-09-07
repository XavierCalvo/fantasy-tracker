package com.fantasytracker.controller;

import com.fantasytracker.acquisition.PlayerMarketDataException;
import com.fantasytracker.acquisition.PlayerMarketDataScraper;
import com.fantasytracker.acquisition.PlayerMarketPrice;
import com.fantasytracker.dto.BulkPriceRefreshResponse;
import com.fantasytracker.dto.PlayerPriceResponse;
import com.fantasytracker.dto.TrackedPlayerListItemResponse;
import com.fantasytracker.dto.TrackedPlayerRequest;
import com.fantasytracker.dto.TrackedPlayerResponse;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPrice;
import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.model.TrackedPlayerStatus;
import com.fantasytracker.repository.PlayerPriceRepository;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TrackedPlayerRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

@RestController
@Tag(name = "Tracking", description = "Seguimiento personal de jugadores: estado en plantilla/seguimiento, cláusula y notas")
public class TrackedPlayerController {

    private static final Logger log = LoggerFactory.getLogger(TrackedPlayerController.class);

    private final TrackedPlayerRepository trackedPlayerRepository;
    private final PlayerRepository playerRepository;
    private final PlayerPriceRepository playerPriceRepository;
    private final PlayerMarketDataScraper playerMarketDataScraper;

    public TrackedPlayerController(
            TrackedPlayerRepository trackedPlayerRepository,
            PlayerRepository playerRepository,
            PlayerPriceRepository playerPriceRepository,
            PlayerMarketDataScraper playerMarketDataScraper) {
        this.trackedPlayerRepository = trackedPlayerRepository;
        this.playerRepository = playerRepository;
        this.playerPriceRepository = playerPriceRepository;
        this.playerMarketDataScraper = playerMarketDataScraper;
    }

    @Operation(summary = "Buscar seguimiento de un jugador", description = "Devuelve el registro de seguimiento (estado, cláusula, notas) de un jugador concreto")
    @GetMapping("/api/players/{playerId}/tracking")
    public TrackedPlayerResponse getByPlayer(@PathVariable Long playerId) {
        return trackedPlayerRepository.findByPlayerId(playerId)
                .map(TrackedPlayerResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Tracking for player " + playerId + " not found"));
    }

    @Operation(summary = "Listar todos los seguimientos", description = "Devuelve todos los jugadores actualmente en plantilla o en seguimiento, con sus datos básicos y el último precio conocido")
    @GetMapping("/api/tracking")
    public List<TrackedPlayerListItemResponse> listAll() {
        List<TrackedPlayer> tracked = trackedPlayerRepository.findAllWithPlayer();
        List<Long> playerIds = tracked.stream().map(t -> t.getPlayer().getId()).toList();
        Map<Long, PlayerPrice> latestPriceByPlayerId = latestPriceByPlayerId(playerIds);

        return tracked.stream()
                .map(t -> TrackedPlayerListItemResponse.from(t, latestPriceByPlayerId.get(t.getPlayer().getId())))
                .toList();
    }

    /**
     * Groups the price history of the given players by player id, keeping only the most recent
     * observation per player. Relies on {@link PlayerPriceRepository#findByPlayerIdInOrderByCapturedAtDesc}
     * returning rows already sorted by {@code capturedAt} descending, so the first entry seen per
     * player id is the latest one.
     */
    private Map<Long, PlayerPrice> latestPriceByPlayerId(List<Long> playerIds) {
        if (playerIds.isEmpty()) {
            return Map.of();
        }
        Map<Long, PlayerPrice> latest = new HashMap<>();
        for (PlayerPrice price : playerPriceRepository.findByPlayerIdInOrderByCapturedAtDesc(playerIds)) {
            latest.putIfAbsent(price.getPlayer().getId(), price);
        }
        return latest;
    }

    @Operation(summary = "Añadir jugador a seguimiento", description = "Empieza a hacer seguimiento de un jugador ya existente en el catálogo (plantilla o seguimiento, con cláusula y notas opcionales)")
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

    @Operation(summary = "Actualizar seguimiento", description = "Modifica el estado, cláusula o notas de un seguimiento existente")
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

    @Operation(summary = "Quitar jugador de seguimiento", description = "Elimina el registro de seguimiento de un jugador (no borra el jugador del catálogo)")
    @DeleteMapping("/api/tracking/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (!trackedPlayerRepository.existsById(id)) {
            throw new NoSuchElementException("Tracking " + id + " not found");
        }
        trackedPlayerRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    /**
     * Refreshes the market price of every actively tracked player in one call (the "Actualizar
     * todos" bulk action). Players marked {@link TrackedPlayerStatus#DISCARDED} are skipped: a
     * manual per-player refresh is still always available, but the bulk action is intentionally
     * limited to players the user still cares about, to avoid needlessly hammering the external
     * source. Unlike the single-player refresh, a scraping failure for one player (e.g. no active
     * market) does not abort the batch: each player is attempted independently and the per-player
     * outcome is reported back to the caller.
     */
    @Operation(summary = "Actualizar precios de todos los jugadores en seguimiento activo", description = "Consulta el valor de mercado actual en la fuente externa para cada jugador en seguimiento activo (excluye los descartados) y registra una nueva observación de precio. Los fallos individuales no interrumpen el resto del lote.")
    @PostMapping("/api/tracking/prices/refresh")
    public BulkPriceRefreshResponse refreshAll() {
        List<TrackedPlayer> tracked = trackedPlayerRepository.findAllWithPlayer().stream()
                .filter(t -> t.getStatus() != TrackedPlayerStatus.DISCARDED)
                .toList();
        List<BulkPriceRefreshResponse.Item> results = new ArrayList<>();

        for (TrackedPlayer trackedPlayer : tracked) {
            Player player = trackedPlayer.getPlayer();
            results.add(refreshOne(player));
        }

        return new BulkPriceRefreshResponse(results);
    }

    private BulkPriceRefreshResponse.Item refreshOne(Player player) {
        String externalId = player.getExternalId();
        if (externalId == null || externalId.isBlank()) {
            return BulkPriceRefreshResponse.Item.failure(
                    player.getId(), player.getName(), "Player has no external id configured");
        }

        try {
            PlayerMarketPrice marketPrice = playerMarketDataScraper.fetchLatestPrice(externalId);
            PlayerPrice price = new PlayerPrice(player, marketPrice.price(), marketPrice.trendAmount(), marketPrice.trendType());
            PlayerPrice saved = playerPriceRepository.save(price);
            return BulkPriceRefreshResponse.Item.success(player.getId(), player.getName(), PlayerPriceResponse.from(saved));
        } catch (PlayerMarketDataException e) {
            log.warn("Bulk price refresh failed for player {} ({}): {}", player.getId(), externalId, e.getMessage());
            return BulkPriceRefreshResponse.Item.failure(player.getId(), player.getName(), e.getMessage());
        } catch (RuntimeException e) {
            log.error("Unexpected error refreshing price for player {} ({})", player.getId(), externalId, e);
            return BulkPriceRefreshResponse.Item.failure(player.getId(), player.getName(), "Unexpected error: " + e.getMessage());
        }
    }
}
