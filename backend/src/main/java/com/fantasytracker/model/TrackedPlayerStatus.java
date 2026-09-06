package com.fantasytracker.model;

public enum TrackedPlayerStatus {
    WATCHING("En seguimiento"),
    OWNED("En plantilla"),
    DISCARDED("Descartado");

    private final String description;

    TrackedPlayerStatus(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
