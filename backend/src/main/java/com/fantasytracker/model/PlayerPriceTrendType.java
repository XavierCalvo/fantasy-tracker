package com.fantasytracker.model;

public enum PlayerPriceTrendType {
    INFLECTION_POSITIVE("Inflexión positiva"),
    ACCELERATING_STRONGLY_UP("Acelera mucho (subida)"),
    ACCELERATING_UP("Acelera (subida)"),
    STABLE_UP("Estable (subida)"),
    DECELERATING_UP("Desacelera (subida)"),
    DECELERATING_STRONGLY_UP("Desacelera mucho (subida)"),
    INFLECTION_NEGATIVE("Inflexión negativa"),
    DECELERATING_STRONGLY_DOWN("Desacelera mucho (bajada)"),
    DECELERATING_DOWN("Desacelera (bajada)"),
    STABLE_DOWN("Estable (bajada)"),
    ACCELERATING_DOWN("Acelera (bajada)"),
    ACCELERATING_STRONGLY_DOWN("Acelera mucho (bajada)");

    private final String description;

    PlayerPriceTrendType(String description) {
        this.description = description;
    }

    public String getDescription() {
        return description;
    }
}
