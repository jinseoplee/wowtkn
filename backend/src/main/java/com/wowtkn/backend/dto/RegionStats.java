package com.wowtkn.backend.dto;

import com.wowtkn.backend.common.Region;

import java.util.List;

/**
 * 특정 지역의 WoW 토큰 가격 통계를 저장하는 DTO
 *
 * @param region 통계 기준 지역 (예: US, EU, KR, TW)
 * @param stats  기간 (예: "6h", "12h", "1d", "1w")별 최고/최저 가격을 저장하는 리스트
 */
public record RegionStats(
        Region region,
        List<PeriodRangeStats> stats
) {
}
