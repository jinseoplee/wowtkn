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
}
