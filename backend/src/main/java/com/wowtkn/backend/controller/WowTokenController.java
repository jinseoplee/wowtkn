package com.wowtkn.backend.controller;

import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.dto.CurrentWowTokenResponse;
import com.wowtkn.backend.dto.RegionStats;
import com.wowtkn.backend.dto.WowTokenHistoryPoint;
import com.wowtkn.backend.service.WowTokenQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wow-tokens")
public class WowTokenController {

    private final WowTokenQueryService wowTokenQueryService;

    @Operation(
            summary = "현재 WoW 토큰 가격 조회",
            description = "지역별 현재 WoW 토큰 가격, 변동량, 변동률, 마지막으로 저장된 시간을 조회합니다."
    )
    @GetMapping
    public ResponseEntity<List<CurrentWowTokenResponse>> getCurrentWowTokens() {
        List<CurrentWowTokenResponse> currentWowTokens = wowTokenQueryService.getCurrentWowTokens();
        return ResponseEntity.ok(currentWowTokens);
    }

    @Operation(
            summary = "WoW 토큰 가격 이력 조회",
            description = "지정된 지역의 WoW 토큰 가격 이력 데이터를 조회합니다."
    )
    @GetMapping("/history")
    public ResponseEntity<List<WowTokenHistoryPoint>> getWowTokenHistoryByRegion(
            @RequestParam(name = "region") Region region,
            @RequestParam(name = "startTime") Long startTime,
            @RequestParam(name = "endTime") Long endTime
    ) {
        List<WowTokenHistoryPoint> wowTokens = wowTokenQueryService.getWowTokensByRegionAndPeriod(region, startTime, endTime);
        return ResponseEntity.ok(wowTokens);
    }

    @Operation(
            summary = "WoW 토큰 가격 통계 조회",
            description = "모든 지역의 WoW 토큰 가격 통계 데이터를 조회합니다."
    )
    @GetMapping("/stats")
    public ResponseEntity<List<RegionStats>> getAllRegionsWowTokenStats() {
        List<RegionStats> stats = wowTokenQueryService.getAllRegionsWowTokenStats();
        return ResponseEntity.ok(stats);
    }
}
