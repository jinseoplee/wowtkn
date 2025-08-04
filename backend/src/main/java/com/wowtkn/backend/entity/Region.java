package com.wowtkn.backend.entity;

public enum Region {
    US("us"),
    EU("eu"),
    KR("kr"),
    TW("tw");

    private final String code;

    Region(String code) {
        this.code = code;
    }

    public String getCode() {
        return code;
    }
}

