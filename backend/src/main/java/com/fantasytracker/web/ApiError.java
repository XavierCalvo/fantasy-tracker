package com.fantasytracker.web;

import java.time.ZonedDateTime;
import java.util.List;

public record ApiError(
        ZonedDateTime timestamp,
        int status,
        String error,
        String message,
        String path,
        List<String> details
) {
    public ApiError(int status, String error, String message, String path, List<String> details) {
        this(ZonedDateTime.now(), status, error, message, path, details);
    }
}
