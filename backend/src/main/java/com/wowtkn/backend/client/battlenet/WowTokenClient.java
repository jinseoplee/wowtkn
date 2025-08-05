package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.BattleNetWowTokenResponse;
import com.wowtkn.backend.entity.Region;

public interface WowTokenClient {

    BattleNetWowTokenResponse getWowToken(Region region, String accessToken);
}
