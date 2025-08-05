package com.wowtkn.backend.service;

import com.wowtkn.backend.client.battlenet.AccessTokenClient;
import com.wowtkn.backend.client.battlenet.WowTokenClient;
import com.wowtkn.backend.client.battlenet.dto.BattleNetWowTokenResponse;
import com.wowtkn.backend.dto.WowTokenResponse;
import com.wowtkn.backend.entity.Region;
import com.wowtkn.backend.entity.WowToken;
import com.wowtkn.backend.repository.WowTokenRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Slf4j
@RequiredArgsConstructor
@Service
public class WowTokenServiceImpl implements WowTokenService {

    private static final int COPPER_TO_GOLD_DIVISOR = 10_000;

    private final AccessTokenClient accessTokenClient;
    private final WowTokenClient wowTokenClient;
    private final WowTokenRepository wowTokenRepository;

    @Scheduled(fixedRateString = "${battlenet.wow-token.save-interval-millis}")
    @Override
    public void saveWowToken() {
        getAccessToken().ifPresent(accessToken -> {
            Arrays.stream(Region.values())
                    .forEach(region -> saveWowTokenForRegion(region, accessToken));
        });
    }

    private Optional<String> getAccessToken() {
        try {
            return Optional.of(accessTokenClient.getAccessToken().accessToken());
        } catch (Exception e) {
            log.error("Failed to get access token", e);
            return Optional.empty();
        }
    }

    private void saveWowTokenForRegion(Region region, String accessToken) {
        try {
            BattleNetWowTokenResponse response = wowTokenClient.getWowToken(region, accessToken);

            if (!wowTokenRepository.existsByRegionAndTimestamp(region, response.lastUpdatedTimestamp())) {
                WowToken wowToken = WowToken.builder()
                        .region(region)
                        .price(convertPrice(response.price()))
                        .timestamp(response.lastUpdatedTimestamp())
                        .build();

                wowTokenRepository.save(wowToken);
            }

        } catch (Exception e) {
            log.error("Failed to save WoW token for region: {}", region, e);
        }
    }

    private int convertPrice(long price) {
        return (int) (price / COPPER_TO_GOLD_DIVISOR);
    }

    @Override
    public List<WowTokenResponse> getWowTokens(Region region, int days) {
        long now = System.currentTimeMillis();
        long from = now - (days * (24 * 60 * 60 * 1000L));

        List<WowToken> wowTokens = wowTokenRepository.findByRegionAndTimestampGreaterThanEqualOrderByTimestampAsc(region, from);

        return wowTokens.stream()
                .map(wowToken -> new WowTokenResponse(wowToken.getPrice(), wowToken.getTimestamp()))
                .toList();
    }
}
