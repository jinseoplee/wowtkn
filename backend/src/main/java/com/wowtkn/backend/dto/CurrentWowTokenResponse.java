package com.wowtkn.backend.dto;

import com.wowtkn.backend.common.Region;

public record CurrentWowTokenResponse(
        Region region,
        long timestamp,
        int price,
        int changeAmount,
        String changeRate
) {
}
