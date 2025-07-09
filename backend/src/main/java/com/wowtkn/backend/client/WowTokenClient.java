package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;

import java.util.Map;

public interface WowTokenClient {

    Map<Region, WowTokenResponse> fetchWowTokens();
}
