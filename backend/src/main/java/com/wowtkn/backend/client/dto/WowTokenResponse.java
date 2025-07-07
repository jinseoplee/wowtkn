package com.wowtkn.backend.client.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record WowTokenResponse(
        Long lastUpdatedTimestamp,
        Long price
) {
}
