package com.wowtkn.backend.service;

import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.dto.CurrentWowTokenResponse;
import com.wowtkn.backend.dto.PeriodRangeStats;
import com.wowtkn.backend.dto.RegionStats;
import com.wowtkn.backend.dto.WowTokenHistoryPoint;
import com.wowtkn.backend.entity.WowToken;
import com.wowtkn.backend.repository.WowTokenRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.*;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Service
public class WowTokenQueryServiceImpl implements WowTokenQueryService {

    private static final Map<String, Long> PERIOD_DURATIONS;

    static {
        Map<String, Long> map = new LinkedHashMap<>();
        map.put("6h", 6L * 60 * 60 * 1000);
        map.put("12h", 12L * 60 * 60 * 1000);
        map.put("1d", 24L * 60 * 60 * 1000);
        map.put("7d", 7L * 24 * 60 * 60 * 1000);
        PERIOD_DURATIONS = Collections.unmodifiableMap(map);
    }

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

    @Override
    public List<RegionStats> getAllRegionsWowTokenStats() {
        final long currentTimeMillis = Instant.now().toEpochMilli();

        // 모든 지역을 순회하며 WoW 토큰 가격 통계를 계산하고 리스트로 반환한다.
        return Arrays.stream(Region.values())
                .map(region -> {
                    // 통계 계산에 필요한 7일치 데이터를 DB에서 조회한다.
                    long sevenDaysAgoMillis = currentTimeMillis - PERIOD_DURATIONS.get("7d");
                    List<WowToken> wowTokens = wowTokenRepository.findByRegionAndTimestampGreaterThanEqualOrderByTimestampDesc(
                            region,
                            sevenDaysAgoMillis
                    );

                    // 조회된 데이터를 바탕으로 기간별 (6h, 12h, 1d, 7d) 최고/최저가 통계를 생성한다.
                    List<PeriodRangeStats> stats = calculatePriceStats(wowTokens, PERIOD_DURATIONS, currentTimeMillis);
                    return new RegionStats(region, stats);
                })
                .toList();
    }

    /**
     * 기간별 WoW 토큰의 최고/최저가 통계를 계산한다.
     *
     * @param wowTokens         {@link WowToken} 리스트
     * @param periodDurations   기간 ("6h", "12h", "1d", "1w")과 해당 기간의 밀리초 단위 값이 저장된 맵
     * @param currentTimeMillis 현재 시각
     * @return
     */
    private List<PeriodRangeStats> calculatePriceStats(
            List<WowToken> wowTokens,
            final Map<String, Long> periodDurations,
            long currentTimeMillis) {

        List<PeriodRangeStats> statsList = new ArrayList<>();

        // 각 정의된 기간 범위(예: 6h, 12h, 1d, 7d)에 대해 통계를 계산한다.
        for (Map.Entry<String, Long> entry : periodDurations.entrySet()) {
            String label = entry.getKey();
            long durationMillis = entry.getValue();

            // 현재 시간 기준, 해당 기간 내의 가격 데이터를 필터링하고 통계를 요약한다.
            IntSummaryStatistics summary = wowTokens.stream()
                    .filter(token -> (currentTimeMillis - token.getTimestamp()) <= durationMillis)
                    .mapToInt(WowToken::getPrice)
                    .summaryStatistics();

            int highPrice = summary.getMax();
            int lowPrice = summary.getMin();

            // 해당 기간에 데이터가 없으면 최고/최저가를 0으로 설정한다.
            if (summary.getCount() == 0) {
                highPrice = 0;
                lowPrice = 0;
            }

            statsList.add(new PeriodRangeStats(label, highPrice, lowPrice));
        }
        return statsList;
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
