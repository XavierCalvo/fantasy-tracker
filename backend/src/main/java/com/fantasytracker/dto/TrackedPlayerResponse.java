package com.fantasytracker.dto;

import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.model.TrackedPlayerStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

public record TrackedPlayerResponse(
        Long id,
        Long playerId,
        TrackedPlayerStatus status,
        BigDecimal clause,
        LocalDate clauseReleaseDate,
        String notes,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt
) {
    public static TrackedPlayerResponse from(TrackedPlayer trackedPlayer) {
        return new TrackedPlayerResponse(
                trackedPlayer.getId(),
                trackedPlayer.getPlayer().getId(),
                trackedPlayer.getStatus(),
                trackedPlayer.getClause(),
                trackedPlayer.getClauseReleaseDate(),
                trackedPlayer.getNotes(),
                trackedPlayer.getCreatedAt(),
                trackedPlayer.getUpdatedAt()
        );
    }
}
