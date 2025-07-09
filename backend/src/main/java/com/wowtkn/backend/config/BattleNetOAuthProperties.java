package com.wowtkn.backend.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Getter
@Setter
@Configuration
@ConfigurationProperties(prefix = "battlenet.oauth")
public class BattleNetOAuthProperties {
    private String tokenUri;
    private String clientId;
    private String clientSecret;
}
