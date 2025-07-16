package com.wowtkn.backend.dto;

/**
 * 기간별 최고/최저 가격을 저장하는 DTO
 *
 * @param period 기간 ("6h", "12h", "1d", "7d")
 * @param high   최고 가격 (골드)
 * @param low    최저 가격 (골드)
 */
public record PeriodRangeStats(
        String period,
        int high,
        int low
) {
}
