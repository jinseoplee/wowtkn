package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;
import reactor.core.publisher.Mono;

import java.util.Map;

public interface WowTokenClient {

    /**
     * 각 지역별 WoW 토큰 데이터를 비동기 방식으로 가져온다.
     *
     * @return {@link Region}을 키로, {@link WowTokenResponse}을 값으로 갖는 Map 타입의 Mono 객체
     */
    Mono<Map<Region, WowTokenResponse>> fetchWowTokens();
}
