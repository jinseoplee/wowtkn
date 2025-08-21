package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.BattleNetWowTokenResponse;
import com.wowtkn.backend.entity.Region;
import reactor.core.publisher.Mono;

public interface WowTokenClient {

    Mono<BattleNetWowTokenResponse> getWowToken(Region region, String accessToken);
}
