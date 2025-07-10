package com.wowtkn.backend.controller;

import com.wowtkn.backend.dto.CurrentWowTokenResponse;
import com.wowtkn.backend.service.WowTokenQueryService;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
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
}
