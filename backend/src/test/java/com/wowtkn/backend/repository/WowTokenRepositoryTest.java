package com.wowtkn.backend.repository;

import com.wowtkn.backend.common.Region;
import com.wowtkn.backend.entity.WowToken;
import com.wowtkn.backend.repository.projection.RegionMaxTimestamp;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class WowTokenRepositoryTest {

    @Autowired
    private WowTokenRepository wowTokenRepository;

    @Test
    @DisplayName("지역별 가장 최근 타임스탬프를 반환해야 한다")
    void shouldReturnMaxTimestampByRegion() {
        // given
        wowTokenRepository.save(WowToken.builder()
                .region(Region.US)
                .timestamp(1000L)
                .price(1000)
                .build());

        wowTokenRepository.save(WowToken.builder()
                .region(Region.US)
                .timestamp(2000L)
                .price(2000)
                .build());

        wowTokenRepository.save(WowToken.builder()
                .region(Region.KR)
                .timestamp(3000L)
                .price(3000)
                .build());

        // when
        List<RegionMaxTimestamp> result = wowTokenRepository.findMaxTimestampGroupByRegion();

        // then
        assertThat(result).hasSize(2);

        assertThat(result).filteredOn(p -> p.getRegion() == Region.US)
                .extracting("maxTimestamp")
                .containsExactly(2000L);

        assertThat(result).filteredOn(p -> p.getRegion() == Region.KR)
                .extracting("maxTimestamp")
                .containsExactly(3000L);
    }
}