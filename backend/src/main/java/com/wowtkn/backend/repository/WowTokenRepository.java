package com.wowtkn.backend.repository;

import com.wowtkn.backend.entity.Region;
import com.wowtkn.backend.entity.WowToken;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface WowTokenRepository extends JpaRepository<WowToken, Integer> {

    boolean existsByRegionAndTimestamp(Region region, long timestamp);

    List<WowToken> findByRegionAndTimestampGreaterThanEqualOrderByTimestampAsc(Region region, long timestamp);
}