package com.wowtkn.backend.service;

import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.dto.CurrentWowTokenResponse;
import com.wowtkn.backend.dto.RegionStats;
import com.wowtkn.backend.dto.WowTokenHistoryPoint;

import java.util.List;

public interface WowTokenQueryService {

    /**
     * 리전별 현재 WoW 토큰 데이터 (가격, 변동량, 변동률, 마지막으로 저장된 시간)을 반환한다.
     *
     * @return 리전별 현재 WoW 토큰 데이터 리스트
     */
    List<CurrentWowTokenResponse> getCurrentWowTokens();

    /**
     * 지정된 지역의 특정 기간에 해당하는 WoW 토큰 가격 이력을 조회한다.
     *
     * @param region         조회할 지역
     * @param startTimestamp 조회 시작 타임스탬프 (밀리초 단위)
     * @param endTimestamp   조회 종료 타임스탬프 (밀리초 단위)
     * @return 조회 기간 내 WoW 토큰 가격 이력 리스트
     */
    List<WowTokenHistoryPoint> getWowTokensByRegionAndPeriod(Region region, Long startTimestamp, Long endTimestamp);

    /**
     * 모든 지역의 WoW 토큰 가격 통계를 계산하여 반환한다.
     * 각 지역별로 미리 정의된 기간(예: 6시간, 12시간, 1일, 1주) 동안의 최고가와 최저가를 포함한다.
     *
     * @return {@link RegionStats} 리스트
     */
    List<RegionStats> getAllRegionsWowTokenStats();
}
