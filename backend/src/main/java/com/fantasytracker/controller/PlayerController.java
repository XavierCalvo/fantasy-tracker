package com.fantasytracker.controller;

import com.fantasytracker.dto.PlayerRequest;
import com.fantasytracker.dto.PlayerResponse;
import com.fantasytracker.model.Player;
import com.fantasytracker.model.Team;
import com.fantasytracker.repository.PlayerRepository;
import com.fantasytracker.repository.TeamRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

@RestController
@RequestMapping("/api/players")
@Tag(name = "Players", description = "Catálogo de jugadores: alta, edición, consulta y baja")
public class PlayerController {
    private final PlayerRepository playerRepository;
    private final TeamRepository teamRepository;

    public PlayerController(PlayerRepository playerRepository, TeamRepository teamRepository) {
        this.playerRepository = playerRepository;
        this.teamRepository = teamRepository;
    }

    @Operation(summary = "Listar jugadores", description = "Devuelve todos los jugadores del catálogo, estén o no en seguimiento")
    @GetMapping
    public List<PlayerResponse> list() {
        return playerRepository.findAll().stream().map(PlayerResponse::from).toList();
    }

    @Operation(summary = "Buscar jugador por id", description = "Devuelve los datos de un jugador concreto")
    @GetMapping("/{id}")
    public PlayerResponse get(@PathVariable Long id) {
        return playerRepository.findById(id).map(PlayerResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Player " + id + " not found"));
    }

    @Operation(summary = "Crear jugador", description = "Da de alta un nuevo jugador en el catálogo (nombre, equipo y posición)")
    @PostMapping
    public ResponseEntity<PlayerResponse> create(@Valid @RequestBody PlayerRequest request) {
        Team team = resolveTeam(request.teamId());
        Player player = new Player(request.name(), team, request.position(), request.externalId());
        Player saved = playerRepository.save(player);
        return ResponseEntity.status(HttpStatus.CREATED).body(PlayerResponse.from(saved));
    }

    @Operation(summary = "Actualizar jugador", description = "Modifica los datos de un jugador existente (nombre, equipo y posición)")
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

    @Operation(summary = "Eliminar jugador", description = "Borra un jugador del catálogo (y su seguimiento/historial de precios asociados)")
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
