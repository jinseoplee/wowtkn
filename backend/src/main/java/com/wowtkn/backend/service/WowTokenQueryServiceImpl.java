package com.wowtkn.backend.service;

import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.dto.CurrentWowTokenResponse;
import com.wowtkn.backend.dto.WowTokenHistoryPoint;
import com.wowtkn.backend.entity.WowToken;
import com.wowtkn.backend.repository.WowTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WowTokenQueryServiceImpl implements WowTokenQueryService {

    private final WowTokenRepository wowTokenRepository;

    @Override
    public List<CurrentWowTokenResponse> getCurrentWowTokens() {
        // 지역별 가장 마지막에 저장된 WoW 토큰 2개를 조회한다.
        List<WowToken> latestTokensByRegion = wowTokenRepository.findLatestTwoTokensByRegion();

        // 조회된 토큰들을 지역별로 그룹화한다.
        Map<Region, List<WowToken>> groupedTokens = groupTokensByRegion(latestTokensByRegion);

        return groupedTokens.entrySet().stream()
                .map(entry -> {
                    Region region = entry.getKey();
                    List<WowToken> tokens = entry.getValue();

                    // 현재 토큰
                    WowToken currentToken = tokens.get(0);

                    // 이전 토큰 (존재할 수도, 아닐 수도 있음)
                    Optional<WowToken> previousToken = tokens.size() >= 2 ? Optional.of(tokens.get(1)) : Optional.empty();

                    // 가격 변동 정보 계산
                    PriceChangeInfo priceChangeInfo = calculateWowTokenPriceChange(currentToken.getPrice(), previousToken);

                    // 응답 DTO 생성
                    return new CurrentWowTokenResponse(
                            region,
                            currentToken.getTimestamp(),
                            currentToken.getPrice(),
                            priceChangeInfo.changeAmount,
                            priceChangeInfo.changeRate
                    );
                })
                .toList();
    }

    @Override
    public List<WowTokenHistoryPoint> getWowTokensByRegionAndPeriod(Region region, Long startTimestamp, Long endTimestamp) {
        List<WowToken> tokens = wowTokenRepository.findByRegionAndTimestampBetweenOrderByTimestampAsc(
                region,
                startTimestamp,
                endTimestamp
        );

        return tokens.stream()
                .map(token -> new WowTokenHistoryPoint(token.getTimestamp(), token.getPrice()))
                .toList();
    }

    /**
     * WoW 토큰 리스트를 지역별로 그룹화한다.
     *
     * @param tokens 그룹화할 {@link WowToken} 리스트
     * @return 지역별로 그룹화된 {@link WowToken} 리스트를 담은 맵
     */
    private Map<Region, List<WowToken>> groupTokensByRegion(List<WowToken> tokens) {
        return tokens.stream()
                .collect(Collectors.groupingBy(WowToken::getRegion));
    }

    /**
     * 현재 WoW 토큰 가격과 이전 WoW 토큰 데이터를 기반으로 가격 변동량과 변동률을 계산한다.
     *
     * @param currentPrice  현재 WoW 토큰의 가격
     * @param previousToken 이전 WoW 토큰 데이터 (Optional로 존재 여부 표현)
     * @return 계산된 변동량과 포맷팅된 변동률을 포함하는 {@link PriceChangeInfo} 객체
     */
    private PriceChangeInfo calculateWowTokenPriceChange(int currentPrice, Optional<WowToken> previousToken) {
        int changeAmount = 0;
        double changeRate = 0.0;

        if (previousToken.isPresent()) {
            int previousPrice = previousToken.get().getPrice();

            changeAmount = currentPrice - previousPrice;
            if (previousPrice != 0) {
                changeRate = ((double) changeAmount / previousPrice) * 100.0;
            }
        }
        return new PriceChangeInfo(changeAmount, changeRate);
    }

    /**
     * 가격 변동량과 변동률을 전달하기 위한 내부 레코드
     */
    private record PriceChangeInfo(int changeAmount, double changeRate) {
    }
}
