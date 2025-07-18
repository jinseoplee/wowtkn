package com.wowtkn.backend.service;

import com.wowtkn.backend.client.WowTokenClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;
import reactor.core.scheduler.Schedulers;

@Slf4j
@RequiredArgsConstructor
@Service
public class WowTokenCollectServiceImpl implements WowTokenCollectService {

    private final WowTokenClient wowTokenClient;
    private final WowTokenSaveService wowTokenSaveService;

    @Scheduled(cron = "0 * * * * *")
    @Override
    public void collectWowTokens() {
        wowTokenClient.fetchWowTokens()
                .flatMap(wowTokens ->
                        Mono.fromRunnable(() -> wowTokenSaveService.saveWowTokens(wowTokens))
                                .subscribeOn(Schedulers.boundedElastic())
                )
                .doOnError(e -> log.error("WoW 토큰 데이터 수집 및 저장 중 오류가 발생했습니다.", e))
                .subscribe();
    }
}
