package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.BattleNetOAuthResponse;
import com.wowtkn.backend.config.BattleNetOAuthProperties;
import com.wowtkn.backend.exception.BattleNetOAuthException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;

@RequiredArgsConstructor
@Component
public class BattleNetOAuthClientImpl implements BattleNetOAuthClient {

    private final RestTemplate restTemplate;
    private final BattleNetOAuthProperties properties;

    @Override
    public BattleNetOAuthResponse fetchOAuthToken() {
        HttpEntity<MultiValueMap<String, String>> httpEntity = createHttpEntity();

        try {
            ResponseEntity<BattleNetOAuthResponse> response = restTemplate.exchange(
                    properties.getTokenUri(),
                    HttpMethod.POST,
                    httpEntity,
                    BattleNetOAuthResponse.class
            );

            return validateOAuthResponse(response.getBody());

        } catch (HttpClientErrorException e) {
            throw new BattleNetOAuthException("Battle.net OAuth 클라이언트 오류가 발생했습니다.", e);
        } catch (HttpServerErrorException e) {
            throw new BattleNetOAuthException("Battle.net OAuth 서버 오류가 발생했습니다.", e);
        } catch (ResourceAccessException e) {
            throw new BattleNetOAuthException("Battle.net OAuth 서버에 연결할 수 없습니다.", e);
        } catch (Exception e) {
            throw new BattleNetOAuthException("Battle.net OAuth 토큰 요청 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    private BattleNetOAuthResponse validateOAuthResponse(BattleNetOAuthResponse response) {
        if (response == null) {
            throw new BattleNetOAuthException("Battle.net OAuth 응답 본문이 비어있습니다.");
        }

        if (response.accessToken() == null || response.expiresIn() == null) {
            throw new BattleNetOAuthException("Battle.net OAuth 필수 응답값이 누락되었습니다.");
        }

        return response;
    }

    private HttpEntity<MultiValueMap<String, String>> createHttpEntity() {
        HttpHeaders headers = createHeaders();
        MultiValueMap<String, String> body = createBody();
        return new HttpEntity<>(body, headers);
    }

    private HttpHeaders createHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setBasicAuth(
                properties.getClientId(),
                properties.getClientSecret()
        );
        headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
        return headers;
    }

    private MultiValueMap<String, String> createBody() {
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        body.set("grant_type", "client_credentials");
        return body;
    }
}
