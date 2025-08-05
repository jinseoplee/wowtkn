package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.BattleNetWowTokenResponse;
import com.wowtkn.backend.config.WowTokenProperties;
import com.wowtkn.backend.entity.Region;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class WowTokenClientImpl implements WowTokenClient {

    private final WebClient webClient;
    private final WowTokenProperties properties;

    @Override
    public BattleNetWowTokenResponse getWowToken(Region region, String accessToken) {
        String url = properties.getUrl().replace("{region}", region.getCode());

        return webClient.get()
                .uri(url)
                .header("Authorization", "Bearer " + accessToken)
                .retrieve()
                .bodyToMono(BattleNetWowTokenResponse.class)
                .block();
    }
}
