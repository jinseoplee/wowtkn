package com.wowtkn.backend.repository;

import com.wowtkn.backend.entity.WowToken;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WowTokenRepository extends JpaRepository<WowToken, Integer> {
}
