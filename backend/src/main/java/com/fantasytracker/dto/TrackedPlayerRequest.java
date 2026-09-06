package com.fantasytracker.dto;

import com.fantasytracker.model.TrackedPlayerStatus;
import jakarta.validation.constraints.PositiveOrZero;

import java.math.BigDecimal;
import java.time.LocalDate;

public record TrackedPlayerRequest(
        TrackedPlayerStatus status,

        @PositiveOrZero(message = "Clause must be non-negative")
        BigDecimal clause,

        LocalDate clauseReleaseDate,

        String notes
) {
}
