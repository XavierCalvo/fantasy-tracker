package com.fantasytracker.model;

import jakarta.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "player")
public class Player {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String team;
    private String position;
    @Column(name = "external_id")
    private String externalId;
    @Column(name = "created_at", nullable = false)
    private Instant createdAt = Instant.now();

    public Player() {}

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }
    public String getTeam() { return team; }
    public void setTeam(String team) { this.team = team; }
    public String getPosition() { return position; }
    public void setPosition(String position) { this.position = position; }
    public String getExternalId() { return externalId; }
    public void setExternalId(String externalId) { this.externalId = externalId; }
    public Instant getCreatedAt() { return createdAt; }
    public void setCreatedAt(Instant createdAt) { this.createdAt = createdAt; }
}
