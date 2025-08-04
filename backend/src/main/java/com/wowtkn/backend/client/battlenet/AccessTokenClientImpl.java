package com.wowtkn.backend.client.battlenet;

import com.wowtkn.backend.client.battlenet.dto.AccessTokenResponse;
import com.wowtkn.backend.config.BattlenetOauthProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;

@RequiredArgsConstructor
@Component
public class AccessTokenClientImpl implements AccessTokenClient {

    private static final String GRANT_TYPE = "grant_type";
    private static final String CLIENT_CREDENTIALS = "client_credentials";

    private final WebClient webClient;
    private final BattlenetOauthProperties properties;

    @Override
    public AccessTokenResponse getAccessToken() {
        return webClient.post()
                .uri(properties.getTokenUrl())
                .headers(headers -> headers.setBasicAuth(
                        properties.getClientId(),
                        properties.getClientSecret()
                ))
                .body(BodyInserters.fromFormData(GRANT_TYPE, CLIENT_CREDENTIALS))
                .retrieve()
                .bodyToMono(AccessTokenResponse.class)
                .block();
    }
}
