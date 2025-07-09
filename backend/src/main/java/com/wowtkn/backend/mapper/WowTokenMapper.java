package com.wowtkn.backend.mapper;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.entity.WowToken;
import org.springframework.stereotype.Component;

@Component
public class WowTokenMapper {

    public WowToken toEntity(Region region, WowTokenResponse response) {
        return WowToken.builder()
                .region(region)
                .timestamp(response.lastUpdatedTimestamp())
                .price((int) (response.price() / 10_000))
                .build();
    }
}
