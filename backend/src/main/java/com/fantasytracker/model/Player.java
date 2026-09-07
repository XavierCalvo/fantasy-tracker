package com.fantasytracker.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "player", indexes = {
    @Index(name = "idx_player_external_id", columnList = "external_id", unique = true)
})
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    @ManyToOne
    @JoinColumn(name = "team_id")
    private Team team;

    @Enumerated(EnumType.STRING)
    @Column(length = 20)
    private PlayerPosition position;

    @Column(name = "external_id", length = 100, unique = true)
    private String externalId;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    // Constructors
    public Player() {
    }

    public Player(String name) {
        this.name = name;
    }

    public Player(String name, Team team, PlayerPosition position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }

    public Player(String name, Team team, PlayerPosition position, String externalId) {
        this.name = name;
        this.team = team;
        this.position = position;
        this.externalId = externalId;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        if (name == null || name.isBlank()) {
            throw new IllegalArgumentException("Player name is required");
        }
        this.name = name;
    }

    public Team getTeam() {
        return team;
    }

    public void setTeam(Team team) {
        this.team = team;
    }

    public PlayerPosition getPosition() {
        return position;
    }

    public void setPosition(PlayerPosition position) {
        this.position = position;
    }

    public String getExternalId() {
        return externalId;
    }

    public void setExternalId(String externalId) {
        this.externalId = externalId;
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }
}
