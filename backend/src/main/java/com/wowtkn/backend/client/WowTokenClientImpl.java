package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.config.WowTokenApiProperties;
import com.wowtkn.backend.exception.WowTokenApiException;
import com.wowtkn.backend.token.BattleNetOAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.util.UriComponentsBuilder;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WowTokenClientImpl implements WowTokenClient {

    private final WebClient webClient;
    private final WowTokenApiProperties properties;
    private final BattleNetOAuthTokenProvider battleNetOAuthTokenProvider;

    @Override
    public Mono<Map<Region, WowTokenResponse>> fetchWowTokens() {
        return battleNetOAuthTokenProvider.getAccessToken()
                .flatMap(accessToken -> {
                    return Flux.fromArray(Region.values())
                            .flatMap(region -> fetchWowToken(region, accessToken)
                                    .doOnError(e -> log.error("{} 지역에서 WoW 토큰 데이터를 가져오는 데 실패했습니다.", region, e))
                                    .onErrorResume(e -> Mono.empty())
                                    .map(response -> Map.entry(region, response))
                            )
                            .collectMap(Map.Entry::getKey, Map.Entry::getValue);
                });
    }

    private Mono<WowTokenResponse> fetchWowToken(Region region, String accessToken) {
        String url = buildWowTokenApiUrl(region);
        return webClient
                .get()
                .uri(url)
                .headers(headers -> headers.setBearerAuth(accessToken))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> Mono.error(new WowTokenApiException("WoW 토큰 API 호출 중 클라이언트 오류가 발생했습니다."))
                )
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new WowTokenApiException("WoW 토큰 API 호출 중 서버 오류가 발생했습니다."))
                )
                .bodyToMono(WowTokenResponse.class)
                .flatMap(this::validateResponse);
    }

    private Mono<WowTokenResponse> validateResponse(WowTokenResponse response) {
        if (response == null) {
            return Mono.error(new WowTokenApiException("WoW 토큰 응답 본문이 비어있습니다."));
        }
        if (response.lastUpdatedTimestamp() == null || response.price() == null) {
            return Mono.error(new WowTokenApiException("WoW 토큰 필수 응답값이 누락되었습니다."));
        }
        return Mono.just(response);
    }

    private String buildWowTokenApiUrl(Region region) {
        String baseUrl = properties.getBaseUrl();
        String namespace = properties.getNamespace();

        return UriComponentsBuilder.fromUriString(baseUrl)
                .uriVariables(Map.of("region", region.name().toLowerCase()))
                .queryParam("namespace", namespace.replace("{region}", region.name().toLowerCase()))
                .build()
                .toUriString();
    }
}
