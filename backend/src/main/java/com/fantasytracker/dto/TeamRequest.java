package com.fantasytracker.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record TeamRequest(
        @NotBlank(message = "Team name is required")
        @Size(max = 255, message = "Team name must be at most 255 characters")
        String name
) {
}
