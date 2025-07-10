package com.wowtkn.backend.repository;

import com.wowtkn.backend.entity.WowToken;
import com.wowtkn.backend.repository.projection.RegionMaxTimestamp;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface WowTokenRepository extends JpaRepository<WowToken, Integer> {

    @Query("""
            SELECT
                wt.region AS region,
                MAX(wt.timestamp) AS maxTimestamp
            FROM
                WowToken AS wt
            GROUP BY
                wt.region
            """)
    List<RegionMaxTimestamp> findMaxTimestampGroupByRegion();

    /**
     * 지역별 가장 최근에 저장된 WoW 토큰 데이터 2개를 조회한다.
     *
     * @return 지역별 가장 최근에 저장된 WoW 토큰 데이터 2개를 포함하는 리스트
     */
    @Query(value = """
            SELECT
                wt.id,
                wt.region,
                wt.timestamp,
                wt.price
            FROM (
                SELECT
                    *,
                    ROW_NUMBER() OVER (PARTITION BY region ORDER BY timestamp DESC) as rn
                FROM
                    wow_token
            ) AS wt
            WHERE
                wt.rn <= 2
            ORDER BY
                wt.region, wt.timestamp DESC
            """, nativeQuery = true)
    List<WowToken> findLatestTwoTokensByRegion();
}
