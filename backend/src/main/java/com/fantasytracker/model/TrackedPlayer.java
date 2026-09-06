package com.fantasytracker.model;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Entity
@Table(name = "tracked_player", indexes = {
    @Index(name = "idx_tracked_player_player", columnList = "player_id"),
    @Index(name = "idx_tracked_player_status", columnList = "status")
})
public class TrackedPlayer {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 32)
    private TrackedPlayerStatus status = TrackedPlayerStatus.WATCHING;

    @Column(name = "clause")
    private BigDecimal clause;

    @Column(name = "clause_release_date")
    private LocalDate clauseReleaseDate;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(nullable = false)
    private ZonedDateTime updatedAt = ZonedDateTime.now();

    // Constructors
    public TrackedPlayer() {
    }

    public TrackedPlayer(Player player) {
        this.player = player;
        this.status = TrackedPlayerStatus.WATCHING;
    }

    public TrackedPlayer(Player player, TrackedPlayerStatus status) {
        this.player = player;
        this.status = status;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        if (player == null) {
            throw new IllegalArgumentException("Player is required");
        }
        this.player = player;
    }

    public TrackedPlayerStatus getStatus() {
        return status;
    }

    public void setStatus(TrackedPlayerStatus status) {
        if (status == null) {
            throw new IllegalArgumentException("Status is required");
        }
        this.status = status;
        this.updatedAt = ZonedDateTime.now();
    }

    public BigDecimal getClause() {
        return clause;
    }

    public void setClause(BigDecimal clause) {
        if (clause != null && clause.signum() < 0) {
            throw new IllegalArgumentException("Clause must be non-negative");
        }
        this.clause = clause;
        this.updatedAt = ZonedDateTime.now();
    }

    public LocalDate getClauseReleaseDate() {
        return clauseReleaseDate;
    }

    public void setClauseReleaseDate(LocalDate clauseReleaseDate) {
        this.clauseReleaseDate = clauseReleaseDate;
        this.updatedAt = ZonedDateTime.now();
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
        this.updatedAt = ZonedDateTime.now();
    }

    public ZonedDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(ZonedDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public ZonedDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(ZonedDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }
}
