package com.wowtkn.backend.entity;

import com.wowtkn.backend.common.Region;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Entity
@Table(
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"region", "timestamp"})
        }
)
public class WowToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    private Long timestamp;

    @Column(nullable = false)
    private Integer price;

    @Builder
    public WowToken(Region region, Long timestamp, Integer price) {
        this.region = region;
        this.timestamp = timestamp;
        this.price = price;
    }
}
