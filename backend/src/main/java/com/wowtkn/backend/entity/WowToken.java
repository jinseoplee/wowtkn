package com.wowtkn.backend.entity;

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
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "wow_token_seq_gen")
    @SequenceGenerator(
            name = "wow_token_seq_gen",
            sequenceName = "wow_token_seq",
            initialValue = 1,
            allocationSize = 50
    )
    private Integer id;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Region region;

    @Column(nullable = false)
    private int price;

    @Column(nullable = false)
    private long timestamp;

    @Builder
    public WowToken(Region region, int price, long timestamp) {
        this.region = region;
        this.price = price;
        this.timestamp = timestamp;
    }
}
