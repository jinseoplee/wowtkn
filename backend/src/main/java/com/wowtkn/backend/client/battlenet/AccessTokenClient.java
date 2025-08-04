package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.AccessTokenResponse;

public interface AccessTokenClient {

    AccessTokenResponse getAccessToken();
}
