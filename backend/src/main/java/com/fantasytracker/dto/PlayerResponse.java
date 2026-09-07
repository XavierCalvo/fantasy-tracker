package com.fantasytracker.dto;

import com.fantasytracker.model.Player;
import com.fantasytracker.model.PlayerPosition;
import com.fantasytracker.model.Team;

import java.time.ZonedDateTime;

public record PlayerResponse(
        Long id,
        String name,
        Long teamId,
        String teamName,
        PlayerPosition position,
        String externalId,
        ZonedDateTime createdAt
) {
    public static PlayerResponse from(Player player) {
        Team team = player.getTeam();
        return new PlayerResponse(
                player.getId(),
                player.getName(),
                team != null ? team.getId() : null,
                team != null ? team.getName() : null,
                player.getPosition(),
                player.getExternalId(),
                player.getCreatedAt()
        );
    }
}
