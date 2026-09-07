package com.fantasytracker.model;

import jakarta.persistence.*;

/**
 * Minimal team catalog (id + name only). Maintained manually, expected to
 * change at most once per season, so no extra attributes are modeled yet.
 */
@Entity
@Table(name = "team", indexes = {
    @Index(name = "idx_team_name", columnList = "name", unique = true)
})
public class Team {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, length = 255)
    private String name;

    public Team() {
    }

    public Team(String name) {
        setName(name);
    }

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
            throw new IllegalArgumentException("Team name is required");
        }
        this.name = name;
    }
}
