package com.wowtkn.backend.controller;

import com.wowtkn.backend.dto.WowTokenResponse;
import com.wowtkn.backend.entity.Region;
import com.wowtkn.backend.service.WowTokenService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/wow-tokens")
public class WowTokenController {

    private final WowTokenService wowTokenService;

    @GetMapping("{region}")
    public ResponseEntity<List<WowTokenResponse>> getWowTokens(@PathVariable("region") Region region,
                                                               @RequestParam(name = "days", defaultValue = "7") int days) {
        List<WowTokenResponse> wowTokens = wowTokenService.getWowTokens(region, days);
        return ResponseEntity.ok(wowTokens);
    }
}
