package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.WowTokenResponse;
import com.wowtkn.backend.entity.Region;

public interface WowTokenClient {

    WowTokenResponse getWowToken(Region region, String accessToken);
}
