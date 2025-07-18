package com.wowtkn.backend.token;

import reactor.core.publisher.Mono;

public interface BattleNetOAuthTokenProvider {

    /**
     * Battle.net API 호출에 필요한 액세스 토큰을 비동기 방식으로 제공한다.
     *
     * @return 액세스 토큰 문자열을 포함하는 Mono 객체
     */
    Mono<String> getAccessToken();
}
