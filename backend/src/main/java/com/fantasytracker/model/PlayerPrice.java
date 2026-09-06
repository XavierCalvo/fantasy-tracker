package com.fantasytracker.model;

import jakarta.persistence.*;
import java.time.ZonedDateTime;

@Entity
@Table(name = "player_price", indexes = {
    @Index(name = "idx_player_price_player", columnList = "player_id")
})
public class PlayerPrice {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "player_id", nullable = false)
    private Player player;

    @Column(nullable = false)
    private Long price;

    @Column(name = "trend_amount")
    private Long trendAmount;

    @Enumerated(EnumType.STRING)
    @Column(name = "trend_type", length = 32)
    private PlayerPriceTrendType trendType;

    @Column(nullable = false, updatable = false)
    private ZonedDateTime capturedAt = ZonedDateTime.now();

    // Constructors
    public PlayerPrice() {
    }

    public PlayerPrice(Player player, Long price) {
        this.player = player;
        this.price = price;
    }

    public PlayerPrice(Player player, Long price, Long trendAmount, PlayerPriceTrendType trendType) {
        this.player = player;
        this.price = price;
        this.trendAmount = trendAmount;
        this.trendType = trendType;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Player getPlayer() {
        return player;
    }

    public void setPlayer(Player player) {
        this.player = player;
    }

    public Long getPrice() {
        return price;
    }

    public void setPrice(Long price) {
        if (price == null || price < 0) {
            throw new IllegalArgumentException("Price must be non-negative");
        }
        this.price = price;
    }

    public Long getTrendAmount() {
        return trendAmount;
    }

    public void setTrendAmount(Long trendAmount) {
        this.trendAmount = trendAmount;
    }

    public PlayerPriceTrendType getTrendType() {
        return trendType;
    }

    public void setTrendType(PlayerPriceTrendType trendType) {
        this.trendType = trendType;
    }

    public ZonedDateTime getCapturedAt() {
        return capturedAt;
    }

    public void setCapturedAt(ZonedDateTime capturedAt) {
        this.capturedAt = capturedAt;
    }
}
