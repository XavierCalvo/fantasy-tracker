package com.fantasytracker.dto;

import com.fantasytracker.model.PlayerPosition;
import com.fantasytracker.model.PlayerPrice;
import com.fantasytracker.model.PlayerPriceTrendType;
import com.fantasytracker.model.Team;
import com.fantasytracker.model.TrackedPlayer;
import com.fantasytracker.model.TrackedPlayerStatus;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Enriched view of a tracked player for list/watchlist screens, combining
 * tracking fields with the denormalized player name/team/position so the
 * frontend does not need a separate lookup per row. Also includes the most
 * recent price observation (if any) so the watchlist can show the latest
 * value/trend without a follow-up call per player.
 */
public record TrackedPlayerListItemResponse(
        Long id,
        Long playerId,
        String playerName,
        String playerTeam,
        PlayerPosition playerPosition,
        TrackedPlayerStatus status,
        BigDecimal clause,
        LocalDate clauseReleaseDate,
        String notes,
        ZonedDateTime createdAt,
        ZonedDateTime updatedAt,
        Long latestPrice,
        Long latestTrendAmount,
        PlayerPriceTrendType latestTrendType,
        ZonedDateTime latestPriceCapturedAt
) {
    public static TrackedPlayerListItemResponse from(TrackedPlayer trackedPlayer, PlayerPrice latestPrice) {
        Team team = trackedPlayer.getPlayer().getTeam();
        return new TrackedPlayerListItemResponse(
                trackedPlayer.getId(),
                trackedPlayer.getPlayer().getId(),
                trackedPlayer.getPlayer().getName(),
                team != null ? team.getName() : null,
                trackedPlayer.getPlayer().getPosition(),
                trackedPlayer.getStatus(),
                trackedPlayer.getClause(),
                trackedPlayer.getClauseReleaseDate(),
                trackedPlayer.getNotes(),
                trackedPlayer.getCreatedAt(),
                trackedPlayer.getUpdatedAt(),
                latestPrice != null ? latestPrice.getPrice() : null,
                latestPrice != null ? latestPrice.getTrendAmount() : null,
                latestPrice != null ? latestPrice.getTrendType() : null,
                latestPrice != null ? latestPrice.getCapturedAt() : null
        );
    }
}
