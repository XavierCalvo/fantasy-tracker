package com.fantasytracker.dto;

import com.fantasytracker.model.Player;

import java.time.ZonedDateTime;

public record PlayerResponse(
        Long id,
        String name,
        String team,
        String position,
        String externalId,
        ZonedDateTime createdAt
) {
    public static PlayerResponse from(Player player) {
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                player.getTeam(),
                player.getPosition(),
                player.getExternalId(),
                player.getCreatedAt()
        );
    }
}
