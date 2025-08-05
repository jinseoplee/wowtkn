package com.wowtkn.backend.client.battlenet.dto;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public record BattleNetWowTokenResponse(
        long lastUpdatedTimestamp,
        long price
) {
}
