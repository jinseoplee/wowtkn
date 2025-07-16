package com.wowtkn.backend.dto;

/**
 * WoW 토큰의 최고/최저 가격을 저장하는 DTO
 *
 * @param high 최고 가격 (골드)
 * @param low  최저 가격 (골드)
 */
public record TimeRangeStats(
        int high,
        int low
) {
}
