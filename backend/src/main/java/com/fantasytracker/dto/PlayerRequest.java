package com.fantasytracker.dto;

import com.fantasytracker.model.PlayerPosition;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @NotBlank(message = "Player name is required")
        @Size(max = 255, message = "Player name must be at most 255 characters")
        String name,

        Long teamId,

        PlayerPosition position,

        @Size(max = 100, message = "External id must be at most 100 characters")
        String externalId
) {
}
