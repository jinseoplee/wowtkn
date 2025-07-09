package com.wowtkn.backend.service;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;

import java.util.Map;

public interface WowTokenSaveService {

    void saveWowTokens(Map<Region, WowTokenResponse> wowTokenResponseByRegion);
}
