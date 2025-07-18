package com.wowtkn.backend.token;

import com.wowtkn.backend.client.BattleNetOAuthClient;
import com.wowtkn.backend.client.dto.BattleNetOAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class BattleNetOAuthTokenProviderImpl implements BattleNetOAuthTokenProvider {

    private final BattleNetOAuthClient battleNetOAuthClient;

    @Override
    public Mono<String> getAccessToken() {
        return battleNetOAuthClient
                .fetchOAuthToken()
                .map(BattleNetOAuthResponse::accessToken);
    }
}
