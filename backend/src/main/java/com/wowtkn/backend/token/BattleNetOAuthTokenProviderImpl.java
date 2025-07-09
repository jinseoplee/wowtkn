package com.wowtkn.backend.token;

import com.wowtkn.backend.client.BattleNetOAuthClient;
import com.wowtkn.backend.client.dto.BattleNetOAuthResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BattleNetOAuthTokenProviderImpl implements BattleNetOAuthTokenProvider {

    private final BattleNetOAuthClient battleNetOAuthClient;

    @Override
    public String getAccessToken() {
        BattleNetOAuthResponse response = battleNetOAuthClient.fetchOAuthToken();
        return response.accessToken();
    }
}
