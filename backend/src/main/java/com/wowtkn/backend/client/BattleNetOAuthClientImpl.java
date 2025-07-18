package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.BattleNetOAuthResponse;
import com.wowtkn.backend.config.BattleNetOAuthProperties;
import com.wowtkn.backend.exception.BattleNetOAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@RequiredArgsConstructor
@Component
public class BattleNetOAuthClientImpl implements BattleNetOAuthClient {

    private final WebClient webClient;
    private final BattleNetOAuthProperties properties;

    @Override
    public Mono<BattleNetOAuthResponse> fetchOAuthToken() {
        return webClient.post()
                .uri(properties.getTokenUri())
                .headers(headers -> headers.setBasicAuth(properties.getClientId(), properties.getClientSecret()))
                .contentType(MediaType.APPLICATION_FORM_URLENCODED)
                .body(BodyInserters.fromFormData("grant_type", "client_credentials"))
                .retrieve()
                .onStatus(status -> status.is4xxClientError(),
                        response -> Mono.error(new BattleNetOAuthException("Battle.net OAuth 클라이언트 오류가 발생했습니다."))
                )
                .onStatus(status -> status.is5xxServerError(),
                        response -> Mono.error(new BattleNetOAuthException("Battle.net OAuth 서버 오류가 발생했습니다."))
                )
                .bodyToMono(BattleNetOAuthResponse.class)
                .flatMap(this::validateResponse);
    }

    private Mono<BattleNetOAuthResponse> validateResponse(BattleNetOAuthResponse response) {
        if (response == null) {
            return Mono.error(new BattleNetOAuthException("Battle.net OAuth 응답 본문이 비어있습니다."));
        }
        if (response.accessToken() == null || response.expiresIn() == null) {
            return Mono.error(new BattleNetOAuthException("Battle.net OAuth 필수 응답값이 누락되었습니다."));
        }
        return Mono.just(response);
    }
}

