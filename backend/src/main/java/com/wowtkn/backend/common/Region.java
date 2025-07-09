package com.wowtkn.backend.common;

import lombok.Getter;

@Getter
public enum Region {
    US("us"),
    EU("eu"),
    KR("kr"),
    TW("tw");

    private final String code;

    Region(String code) {
        this.code = code;
    }
}
