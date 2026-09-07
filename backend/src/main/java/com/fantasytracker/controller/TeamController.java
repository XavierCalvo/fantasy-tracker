package com.fantasytracker.controller;

import com.fantasytracker.dto.TeamRequest;
import com.fantasytracker.dto.TeamResponse;
import com.fantasytracker.model.Team;
import com.fantasytracker.repository.TeamRepository;
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
public class TeamController {
    private final TeamRepository teamRepository;

    public TeamController(TeamRepository teamRepository) {
        this.teamRepository = teamRepository;
    }

    @GetMapping
    public List<TeamResponse> list() {
        return teamRepository.findAll(Sort.by(Sort.Direction.ASC, "name")).stream()
                .map(TeamResponse::from)
                .toList();
    }

    @GetMapping("/{id}")
    public TeamResponse get(@PathVariable Long id) {
        return teamRepository.findById(id).map(TeamResponse::from)
                .orElseThrow(() -> new NoSuchElementException("Team " + id + " not found"));
    }

    @PostMapping
    public ResponseEntity<TeamResponse> create(@Valid @RequestBody TeamRequest request) {
        Team team = new Team(request.name());
        Team saved = teamRepository.save(team);
        return ResponseEntity.status(HttpStatus.CREATED).body(TeamResponse.from(saved));
    }

    @PutMapping("/{id}")
    public TeamResponse update(@PathVariable Long id, @Valid @RequestBody TeamRequest request) {
        Team existing = teamRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Team " + id + " not found"));
        existing.setName(request.name());
        return TeamResponse.from(teamRepository.save(existing));
    }
}
