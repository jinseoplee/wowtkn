package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.AccessTokenResponse;
import reactor.core.publisher.Mono;

public interface AccessTokenClient {

    Mono<AccessTokenResponse> getAccessToken();
}
