package com.fantasytracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record PlayerRequest(
        @NotBlank(message = "Player name is required")
        @Size(max = 255, message = "Player name must be at most 255 characters")
        String name,

        @Size(max = 255, message = "Team must be at most 255 characters")
        String team,

        @Size(max = 50, message = "Position must be at most 50 characters")
        String position,

        @Size(max = 100, message = "External id must be at most 100 characters")
        String externalId
) {
}
