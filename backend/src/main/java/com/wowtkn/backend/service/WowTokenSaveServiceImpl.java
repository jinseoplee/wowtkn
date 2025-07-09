package com.wowtkn.backend.service;

import com.wowtkn.backend.client.dto.WowTokenResponse;
import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.entity.WowToken;
import com.wowtkn.backend.mapper.WowTokenMapper;
import com.wowtkn.backend.repository.WowTokenRepository;
import com.wowtkn.backend.repository.projection.RegionMaxTimestamp;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@RequiredArgsConstructor
@Service
public class WowTokenSaveServiceImpl implements WowTokenSaveService {

    private final WowTokenMapper wowTokenMapper;
    private final WowTokenRepository wowTokenRepository;

    @Transactional
    @Override
    public void saveWowTokens(Map<Region, WowTokenResponse> wowTokenResponseByRegion) {
        Map<Region, Long> maxTimestampsByRegion = getMaxTimestampsByRegion();

        List<WowToken> newTokens = wowTokenResponseByRegion.entrySet().stream()
                .filter(entry -> isNew(maxTimestampsByRegion.get(entry.getKey()), entry.getValue()))
                .map(entry -> wowTokenMapper.toEntity(entry.getKey(), entry.getValue()))
                .toList();

        if (!newTokens.isEmpty()) {
            wowTokenRepository.saveAll(newTokens);
            log.info("새로운 WoW 토큰 데이터 {}개를 저장했습니다.", newTokens.size());
        }
    }

    private Map<Region, Long> getMaxTimestampsByRegion() {
        return wowTokenRepository.findMaxTimestampGroupByRegion().stream()
                .collect(Collectors.toMap(
                        RegionMaxTimestamp::getRegion,
                        RegionMaxTimestamp::getMaxTimestamp
                ));
    }

    private boolean isNew(Long maxTimestamp, WowTokenResponse wowTokenResponse) {
        return maxTimestamp == null || wowTokenResponse.lastUpdatedTimestamp() > maxTimestamp;
    }
}
