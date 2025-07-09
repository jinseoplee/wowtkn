package com.wowtkn.backend.client;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.config.WowTokenApiProperties;
import com.wowtkn.backend.exception.WowTokenApiException;
import com.wowtkn.backend.token.BattleNetOAuthTokenProvider;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RequiredArgsConstructor
@Component
public class WowTokenClientImpl implements WowTokenClient {

    private final RestTemplate restTemplate;
    private final WowTokenApiProperties properties;
    private final BattleNetOAuthTokenProvider battleNetOAuthTokenProvider;

    @Override
    public Map<Region, WowTokenResponse> fetchWowTokens() {
        String accessToken = battleNetOAuthTokenProvider.getAccessToken();

        Map<Region, WowTokenResponse> wowTokens = new HashMap<>();

        for (Region region : Region.values()) {
            try {
                WowTokenResponse wowToken = fetchWowToken(region, accessToken);
                wowTokens.put(region, wowToken);
            } catch (WowTokenApiException e) {
                log.error("{} 지역에서 WoW 토큰 데이터를 가져오는 데 실패했습니다.", region, e);
            }
        }

        return wowTokens;
    }

    private WowTokenResponse fetchWowToken(Region region, String accessToken) {
        HttpEntity<Void> httpEntity = createHttpEntity(accessToken);
        String url = buildWowTokenApiUrl(region);

        try {
            ResponseEntity<WowTokenResponse> response = restTemplate.exchange(
                    url,
                    HttpMethod.GET,
                    httpEntity,
                    WowTokenResponse.class
            );

            return validateWowTokenResponse(response.getBody());

        } catch (HttpClientErrorException e) {
            throw new WowTokenApiException("WoW 토큰 API 호출 중 클라이언트 오류가 발생했습니다.", e);
        } catch (HttpServerErrorException e) {
            throw new WowTokenApiException("WoW 토큰 API 호출 중 서버 오류가 발생했습니다.", e);
        } catch (ResourceAccessException e) {
            throw new WowTokenApiException("WoW 토큰 API 서버에 연결할 수 없습니다.", e);
        } catch (Exception e) {
            throw new WowTokenApiException("WoW 토큰 API 요청 중 예상치 못한 오류가 발생했습니다.", e);
        }
    }

    private WowTokenResponse validateWowTokenResponse(WowTokenResponse response) {
        if (response == null) {
            throw new WowTokenApiException("WoW 토큰 응답 본문이 비어있습니다.");
        }

        if (response.lastUpdatedTimestamp() == null || response.price() == null) {
            throw new WowTokenApiException("WoW 토큰 필수 응답값이 누락되었습니다.");
        }

        return response;
    }

    private HttpEntity<Void> createHttpEntity(String accessToken) {
        HttpHeaders headers = createHeaders(accessToken);
        return new HttpEntity<>(headers);
    }

    private HttpHeaders createHeaders(String accessToken) {
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(accessToken);
        return headers;
    }

    private String buildWowTokenApiUrl(Region region) {
        String baseUrlPattern = properties.getBaseUrl();
        String namespacePattern = properties.getNamespace();

        return UriComponentsBuilder.fromUriString(baseUrlPattern)
                .uriVariables(Map.of("region", region.name().toLowerCase()))
                .queryParam("namespace", namespacePattern.replace("{region}", region.name().toLowerCase()))
                .build()
                .toUriString();
    }
}
