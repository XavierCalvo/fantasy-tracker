package com.fantasytracker.controller;

import com.fantasytracker.dto.TeamRequest;
import com.fantasytracker.dto.TeamResponse;
import com.fantasytracker.model.Team;
import com.fantasytracker.repository.TeamRepository;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.NoSuchElementException;

/**
 * Minimal team maintenance (id + name only). Expected to be used at most
 * once per season, so no delete endpoint is exposed yet.
 */
@RestController
@RequestMapping("/api/teams")
@Tag(name = "Teams", description = "Mantenimiento de equipos (alta/edición), usado normalmente una vez por temporada")
public class TeamController {
    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @Operation(summary = "Listar equipos", description = "Devuelve todos los equipos ordenados alfabéticamente por nombre")
    @GetMapping
    public List<TeamResponse> list() {
        return teamRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TeamResponse::from)
                .toList();
    }

    @Operation(summary = "Buscar equipo por id", description = "Devuelve los datos de un equipo concreto")
    @GetMapping("/{id}")
    public TeamResponse get(@PathVariable Long id) {
        return teamRepository.findById(id).map(TeamResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Team " + id + " not found"));
    }

    @Operation(summary = "Crear equipo", description = "Da de alta un nuevo equipo (mantenimiento de temporada)")
    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
        Team team = new Team(request.name());
        Team saved = teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponse.from(saved));
    }

    @Operation(summary = "Actualizar equipo", description = "Modifica el nombre de un equipo existente")
    @PutMapping("/{id}")
    public TeamResponse update(@PathVariable Long id, @Valid @RequestBody TeamRequest request) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team " + id + " not found"));
        existing.setName(request.name());
        return TeamResponse.from(teamRepository.save(existing));
    }
}
