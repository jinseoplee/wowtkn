package com.wowtkn.backend.service;

import com.wowtkn.backend.dto.CurrentWowTokenResponse;

import java.util.List;

public interface WowTokenQueryService {

    /**
     * 리전별 현재 WoW 토큰 데이터 (가격, 변동량, 변동률, 마지막으로 저장된 시간)을 반환한다.
     *
     * @return 리전별 현재 WoW 토큰 데이터 리스트
     */
    List<CurrentWowTokenResponse> getCurrentWowTokens();
}
