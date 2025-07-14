package com.wowtkn.backend.dto;

public record WowTokenHistoryPoint(
        long timestamp,
        int price
) {
}
