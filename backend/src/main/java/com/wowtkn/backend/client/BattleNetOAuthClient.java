package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.BattleNetOAuthResponse;
import reactor.core.publisher.Mono;

public interface BattleNetOAuthClient {

    /**
     * Battle.net OAuth 토큰을 비동기 방식으로 가져온다.
     *
     * @return {@link BattleNetOAuthResponse} 타입의 Mono 객체
     */
    Mono<BattleNetOAuthResponse> fetchOAuthToken();
}
