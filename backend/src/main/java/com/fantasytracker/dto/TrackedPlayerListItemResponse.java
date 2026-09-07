package com.fantasytracker.dto;

import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.model.TrackedPlayerStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Enriched view of a tracked player for list/watchlist screens, combining
 * tracking fields with the denormalized player name/team/position so the
 * frontend does not need a separate lookup per row.
 */
public record TrackedPlayerListItemResponse(
        Long id,
        Long playerId,
        String playerName,
        String playerTeam,
        String playerPosition,
        TrackedPlayerStatus status,
        BigDecimal clause,
        LocalDate clauseReleaseDate,
        String notes,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    public static TrackedPlayerListItemResponse from(TrackedPlayer trackedPlayer) {
        return new TrackedPlayerListItemResponse(
                trackedPlayer.getId(),
                trackedPlayer.getPlayer().getId(),
                trackedPlayer.getPlayer().getName(),
                trackedPlayer.getPlayer().getTeam(),
                trackedPlayer.getPlayer().getPosition(),
                trackedPlayer.getStatus(),
                trackedPlayer.getClause(),
                trackedPlayer.getClauseReleaseDate(),
                trackedPlayer.getNotes(),
                trackedPlayer.getCreatedAt(),
                trackedPlayer.getUpdatedAt()
        );
    }
}
