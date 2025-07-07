package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.BattleNetOAuthResponse;

public interface BattleNetOAuthClient {

    BattleNetOAuthResponse fetchOAuthToken();
}
