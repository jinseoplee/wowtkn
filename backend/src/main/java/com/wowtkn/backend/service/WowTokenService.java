package com.wowtkn.backend.service;

import com.wowtkn.backend.dto.WowTokenResponse;
import com.wowtkn.backend.entity.Region;

import java.util.List;

public interface WowTokenService {

    void saveWowToken();

    List<WowTokenResponse> getWowTokens(Region region, int days);
}
