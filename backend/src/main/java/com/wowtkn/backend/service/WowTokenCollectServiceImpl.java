package com.wowtkn.backend.service;

import com.wowtkn.backend.client.WowTokenClient;
import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Service
public class WowTokenCollectServiceImpl implements WowTokenCollectService {

    private final WowTokenClient wowTokenClient;
    private final WowTokenSaveService wowTokenSaveService;

    @Scheduled(cron = "0 * * * * *")
    @Override
    public void collectWowTokens() {
        try {
            Map<Region, WowTokenResponse> wowTokenResponseByRegion = wowTokenClient.fetchWowTokens();

            wowTokenSaveService.saveWowTokens(wowTokenResponseByRegion);

        } catch (Exception e) {
            log.error("WoW 토큰 데이터 수집 및 저장 중 오류가 발생했습니다.", e);
        }
    }
}
